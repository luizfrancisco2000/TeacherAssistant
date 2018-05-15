package com.example.luiz.teacherassistent.Helper;

import android.graphics.Point;
import android.graphics.Rect;

import com.google.android.gms.vision.text.Text;

import java.util.List;

public class ocr implements Text {
    @Override
    public List<? extends Text> getComponents() {
        return null;
    }

    @Override
    public Point[] getCornerPoints() {
        return new Point[0];
    }

    @Override
    public Rect getBoundingBox() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

}
