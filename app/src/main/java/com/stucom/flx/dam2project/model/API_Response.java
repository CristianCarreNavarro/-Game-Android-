package com.stucom.flx.dam2project.model;

public class API_Response <T>{

        private T data;
        private int count;
        private int errorCode;
        private String errorMsg;

//ESTO ES PARA PODER HACER COSAS CON LA RESPUESTA DE LA API

        public API_Response(T data, int count, int errorCode, String errorMsg) {
            this.data = data;
            this.count = count;
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }

        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public int getErrorCode() { return errorCode; }
        public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
        public String getErrorMsg() { return errorMsg; }
        public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    }



