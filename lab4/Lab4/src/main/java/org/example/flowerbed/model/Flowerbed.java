package org.example.flowerbed.model;

public class Flowerbed {
    private Flower[][] flowers;

    public Flowerbed() {

    }

    public void initArray(int N, int M) {
        this.flowers = new Flower[N][M];
        for (int j = 0; j < N; j++) {
            Flower[] flowerRow = new Flower[M];
            for (int i = 0; i < M; i++) {
                flowerRow[i] = new Flower(Flower.State.GROWING);
            }
            flowers[j] = flowerRow;
        }
    }

    public Flower[][] getFlowers() {
        return flowers;
    }
}
