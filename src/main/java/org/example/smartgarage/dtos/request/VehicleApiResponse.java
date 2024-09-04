package org.example.smartgarage.dtos.request;

import java.util.List;

public class VehicleApiResponse {

    private boolean error;
    private String msg;
    private List<VehiclesRequestDto> data;

    public boolean isError() {
        return error;
    }

    public VehicleApiResponse setError(boolean error) {
        this.error = error;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public VehicleApiResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public List<VehiclesRequestDto> getData() {
        return data;
    }

    public VehicleApiResponse setData(List<VehiclesRequestDto> data) {
        this.data = data;
        return this;
    }
}
