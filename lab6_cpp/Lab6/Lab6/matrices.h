#pragma once
#include <random>
namespace Matrices {
	static std::random_device rd;
	static std::mt19937 gen(rd());
	static std::uniform_real_distribution<double> dis(0.0, 1.0);
	void random(double* matrix, int size) {
		int sq = size * size;
		for (int i = 0; i < sq; i++) {
			matrix[i] = dis(gen);
		}
	}

	void zeros(double* matrix, int size) {
		int sq = size * size;
		for (int i = 0; i < sq; i++) {
			matrix[i] = 0;
		}
	}
	void ones(double* matrix, int size) {
		int sq = size * size;
		for (int i = 0; i < sq; i++) {
			matrix[i] = 1;
		}
	}
}