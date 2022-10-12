/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author guilherme
 */
public class ImageResult {
    public ImageResult(ImageRequest request) {
        this.imageId = UUID.randomUUID().toString();
        this.user = request.userId;
        this.alg = request.algorithm.name();
        this.ImageSize = request.imageSize;
    }
    public String imageId;
    public String user;
    public String alg;
    public String imagePath;
    public LocalDateTime startTime;
    public LocalDateTime finishTime;
    public int ImageSize;
    public int IterNum;
    public int elapsedSeconds;
}
