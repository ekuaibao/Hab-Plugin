#!/usr/bin/env python3

import os
import shutil
import subprocess
import hashlib
import argparse
import xml.etree.ElementTree as ET
from pathlib import Path

def parse_arguments():
    """解析命令行参数"""
    parser = argparse.ArgumentParser(description='创建Maven中央仓库发布包')
    parser.add_argument('--version', '-v', 
                      help='版本号 (例如: 1.1.0)',
                      default=None)
    parser.add_argument('--group-id', '-g',
                      help='Group ID (例如: com.hosecloud.hab)',
                      default="com.hosecloud.hab")
    parser.add_argument('--artifact-id', '-a',
                      help='Artifact ID (例如: plugin)',
                      default="plugin")
    return parser.parse_args()

def get_pom_version():
    """从pom.xml中获取版本号"""
    try:
        tree = ET.parse('pom.xml')
        root = tree.getroot()
        # 处理默认命名空间
        ns = {'maven': 'http://maven.apache.org/POM/4.0.0'}
        version = root.find('./maven:version', ns)
        if version is not None:
            return version.text
        return None
    except Exception as e:
        print(f"错误: 无法从pom.xml读取版本号: {e}")
        return None

# 设置变量
args = parse_arguments()
GROUP_ID = args.group_id
ARTIFACT_ID = args.artifact_id

# 获取版本号
pom_version = get_pom_version()
if pom_version is None:
    print("错误: 无法从pom.xml中读取版本号")
    exit(1)

# 如果命令行没有指定版本号，使用pom.xml中的版本号
VERSION = args.version if args.version is not None else pom_version

# 检查版本号是否匹配
if args.version is not None and args.version != pom_version:
    print(f"错误: 命令行指定的版本号 ({args.version}) 与pom.xml中的版本号 ({pom_version}) 不匹配")
    print("请确保两者一致，或者不指定--version参数来使用pom.xml中的版本号")
    exit(1)

TARGET_DIR = "target"
BUNDLE_DIR = "bundle"
ZIP_NAME = f"{ARTIFACT_ID}-{VERSION}-bundle.zip"

def clean_old_files():
    """清理旧的bundle目录和zip文件"""
    print("清理旧文件...")
    if os.path.exists(BUNDLE_DIR):
        shutil.rmtree(BUNDLE_DIR)
    if os.path.exists(ZIP_NAME):
        os.remove(ZIP_NAME)

def build_project():
    """清理并构建项目"""
    print("清理并构建项目...")
    subprocess.run(["mvn", "clean", "package", "-DskipTests"], check=True)

def create_bundle_structure():
    """创建bundle目录结构"""
    print("创建bundle目录结构...")
    bundle_path = os.path.join(BUNDLE_DIR, *GROUP_ID.split("."), ARTIFACT_ID, VERSION)
    os.makedirs(bundle_path, exist_ok=True)
    return bundle_path

def copy_files(bundle_path):
    """复制文件到bundle目录"""
    print("复制文件到bundle目录...")
    files_to_copy = [
        f"{ARTIFACT_ID}-{VERSION}.jar",
        f"{ARTIFACT_ID}-{VERSION}-sources.jar",
        f"{ARTIFACT_ID}-{VERSION}-javadoc.jar"
    ]
    
    for file in files_to_copy:
        src = os.path.join(TARGET_DIR, file)
        dst = os.path.join(bundle_path, file)
        shutil.copy2(src, dst)
    
    # 复制并重命名pom文件
    pom_dst = os.path.join(bundle_path, f"{ARTIFACT_ID}-{VERSION}.pom")
    shutil.copy2("pom.xml", pom_dst)

def generate_checksums(bundle_path):
    """生成文件的MD5和SHA1校验和"""
    print("生成校验和...")
    for file in os.listdir(bundle_path):
        if file.endswith(('.jar', '.pom')):
            file_path = os.path.join(bundle_path, file)
            
            # 生成MD5
            with open(file_path, 'rb') as f:
                md5_hash = hashlib.md5()
                for chunk in iter(lambda: f.read(4096), b''):
                    md5_hash.update(chunk)
                md5_sum = md5_hash.hexdigest()
            
            with open(f"{file_path}.md5", 'w') as f:
                f.write(md5_sum)
            
            # 生成SHA1
            with open(file_path, 'rb') as f:
                sha1_hash = hashlib.sha1()
                for chunk in iter(lambda: f.read(4096), b''):
                    sha1_hash.update(chunk)
                sha1_sum = sha1_hash.hexdigest()
            
            with open(f"{file_path}.sha1", 'w') as f:
                f.write(sha1_sum)

def generate_gpg_signatures(bundle_path):
    """生成GPG签名"""
    print("生成GPG签名...")
    for file in os.listdir(bundle_path):
        if file.endswith(('.jar', '.pom')):
            file_path = os.path.join(bundle_path, file)
            if os.path.exists(f"{file_path}.asc"):
                os.remove(f"{file_path}.asc")
            subprocess.run(["gpg", "-ab", file_path], 
                         input=b'y\n',  # 自动确认覆盖
                         check=True)

def create_zip():
    """创建最终的zip包"""
    print("打包成zip...")
    print(f"当前目录: {os.getcwd()}")
    print("创建zip文件...")
    
    # 切换到bundle目录并创建zip
    original_dir = os.getcwd()
    os.chdir(BUNDLE_DIR)
    try:
        subprocess.run(["zip", "-r", f"../{ZIP_NAME}", "."], check=True)
    finally:
        os.chdir(original_dir)

def show_bundle_info():
    """显示生成的bundle信息"""
    print(f"Bundle创建完成: {ZIP_NAME}")
    print("生成的文件列表：")
    subprocess.run(["unzip", "-l", ZIP_NAME], check=True)
    print("\nBundle大小：")
    subprocess.run(["ls", "-lh", ZIP_NAME], check=True)

def main():
    try:
        clean_old_files()
        build_project()
        bundle_path = create_bundle_structure()
        copy_files(bundle_path)
        generate_checksums(bundle_path)
        generate_gpg_signatures(bundle_path)
        create_zip()
        show_bundle_info()
    except subprocess.CalledProcessError as e:
        print(f"错误: 执行命令失败: {e}")
        exit(1)
    except Exception as e:
        print(f"错误: {e}")
        exit(1)

if __name__ == "__main__":
    main() 