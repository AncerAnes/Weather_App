package com.barmej.wetherapp.data;

public interface OnDataDeliveryListener<T> {
    void OnDataDelivery(T dataObject);
    void OnErrorOccurred(Throwable throwable);
}
