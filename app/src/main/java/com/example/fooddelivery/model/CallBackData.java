package com.example.fooddelivery.model;

public interface CallBackData{
    void firebaseResponseCallback(boolean result);
    void callbackListOrder();
    void callbackComment(Comment result);
}
