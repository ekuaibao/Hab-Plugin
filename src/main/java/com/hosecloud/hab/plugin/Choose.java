package com.hosecloud.hab.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 节点选择
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Choose {

    private List<String> tags;

    private List<ChooseItem> items;

    private String rawType;

    private ItemType itemType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChooseItem {
        private String label;
        private String code;

        @JsonIgnore
        private ItemType itemType;

        private String rawType;

        private Choose subChoose;

        private String thirdId;

        private List<String> acceptTypes;

        public ChooseItem() {
        }

        public ChooseItem(String label, String code, ItemType itemType, String rawType, Choose subChoose, String thirdId, List<String> acceptTypes) {
            this.label = label;
            this.code = code;
            this.itemType = itemType;
            this.rawType = rawType;
            this.subChoose = subChoose;
            this.thirdId = thirdId;
            this.acceptTypes = acceptTypes;
        }

        public ChooseItem(String label, String code, ItemType itemType) {
            this.label = label;
            this.code = code;
            this.itemType = itemType;
        }

        public ChooseItem(String label, String code, ItemType itemType, String rawType) {
            this.label = label;
            this.code = code;
            this.itemType = itemType;
            this.rawType = rawType;
        }

        public ChooseItem(String label, String code, ItemType itemType, String rawType, Choose subChoose) {
            this.label = label;
            this.code = code;
            this.itemType = itemType;
            this.rawType = rawType;
            this.subChoose = subChoose;
        }

        public ChooseItem(String label, String code, ItemType itemType, String rawType, Choose subChoose, String thirdId) {
            this.label = label;
            this.code = code;
            this.itemType = itemType;
            this.rawType = rawType;
            this.subChoose = subChoose;
            this.thirdId = thirdId;
        }

        public String getType() {
            return rawType == null ? itemType.name() : rawType;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public ItemType getItemType() {
            return itemType;
        }

        public void setItemType(ItemType itemType) {
            this.itemType = itemType;
        }

        public String getRawType() {
            return rawType;
        }

        public void setRawType(String rawType) {
            this.rawType = rawType;
        }

        public Choose getSubChoose() {
            return subChoose;
        }

        public void setSubChoose(Choose subChoose) {
            this.subChoose = subChoose;
        }

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public List<String> getAcceptTypes() {
            return acceptTypes;
        }

        public void setAcceptTypes(List<String> acceptTypes) {
            this.acceptTypes = acceptTypes;
        }
    }

    public enum ItemType {
        TEXT, NUMBER, BOOLEAN, DATE, DATA_RANGE, LIST, MAP, MONEY
    }

    public Choose(List<ChooseItem> items) {
        this.items = items;
    }

    public Choose(List<String> tags, List<ChooseItem> items) {
        this.tags = tags;
        this.items = items;
    }

    public String getType() {
        if (rawType == null && itemType == null) {
            return null;
        }
        return rawType == null ? itemType.name() : rawType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ChooseItem> getItems() {
        return items;
    }

    public void setItems(List<ChooseItem> items) {
        this.items = items;
    }

    public String getRawType() {
        return rawType;
    }

    public void setRawType(String rawType) {
        this.rawType = rawType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
