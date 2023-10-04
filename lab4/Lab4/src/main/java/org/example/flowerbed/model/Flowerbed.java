package org.example.flowerbed.model;

import java.util.Iterator;

public class Flowerbed implements Iterable<Flower> {
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

    @Override
    public Iterator<Flower> iterator() {
        return new FlowerIterator();
    }

    private class FlowerIterator implements Iterator<Flower> {
        private int row = 0;
        private int col = 0;

        @Override
        public boolean hasNext() {
            return row < flowers.length && col < flowers[row].length;
        }

        @Override
        public Flower next() {
            Flower flower = flowers[row][col];
            col++;
            if (col >= flowers[row].length) {
                col = 0;
                row++;
            }
            return flower;
        }
    }

}
