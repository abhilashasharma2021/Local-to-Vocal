package com.localtovocal.Java;

import java.util.List;

public class ListClass {

    private String name;
    private List<MyData> myDataList = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static class MyData {

        private String id;
        private String name;
        private String designation;


        public MyData(String id, String name, String designation) {
            this.id = id;
            this.name = name;
            this.designation = designation;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }
    }

}
