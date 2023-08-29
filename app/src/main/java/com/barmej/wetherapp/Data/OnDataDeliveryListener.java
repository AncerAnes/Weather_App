package com.barmej.wetherapp.Data;

public interface OnDataDeliveryListener<T> {
    void OnDataDelivery(T dataObject);
    void OnErrorOccurred(Throwable throwable);
}
