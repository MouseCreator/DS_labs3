#include "common.h"

void calculate(double* AMatrix, double* BMatrix, double* ABlock, double* BBlock) {
	int psq = sqrt(ProcNum);
	int lim = psq - 1;
	for (int i = 0; i < lim; ++i) {
		for (int j = 0; j < lim; ++j) {
			//sendBlock(ABlock, i, (j - i + psq % psq));
			//sendBlock(BBlock, (j - i + psq % psq), j);
		}
	}
}