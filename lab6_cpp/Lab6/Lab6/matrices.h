#pragma once
#include <random>
namespace Matrices {
	void random(double* matrix, int size) {
		std::random_device rd;
		std::mt19937 gen(rd());
		std::uniform_real_distribution<double> dis(0.0, 1.0);
		int sq = size * size;
		for (int i = 0; i < sq; ++i) {
			matrix[i] = dis(gen);
		}
	}

	void zeros(double* matrix, int size) {
		int sq = size * size;
		for (int i = 0; i < size; ++i) {
			matrix[i] = 0;
		}
	}
	void ones(double* matrix, int size) {
		int sq = size * size;
		for (int i = 0; i < size; ++i) {
			matrix[i] = 1;
		}
	}
}