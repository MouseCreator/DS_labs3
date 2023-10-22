#pragma once
#include "common.h"
namespace Cannon {

	int GridCoords[2];
	int GridSize;

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
	void receiveBlock(double* Matrix, double* Block, int Xcord, int Ycord, int Size, int BlockSize, MPI_Comm comm) {
		int InitialPosition = Ycord * BlockSize * Size + Xcord * BlockSize;
		int CurrentPosition = InitialPosition;
		for (int i = 0; i < BlockSize; ++i, CurrentPosition += Size - BlockSize) {
			MPI_Scatter(&Matrix[CurrentPosition], BlockSize, MPI_DOUBLE,
				&(Block[i * BlockSize]), BlockSize, MPI_DOUBLE, 0, comm);
		}
	}
	void distribute(double* pAMatrix, double* pABlock, double* pBMatrix, double* pBBlock, int Size, int BlockSize) {
		double* MatrixRow = new double[BlockSize * Size];
		int N = GridCoords[0];
		int M = GridCoords[1];
		receiveBlock(pAMatrix, pABlock, N, (N + M) % GridSize, Size, BlockSize, RowComm);
		receiveBlock(pAMatrix, pABlock, (N + M) % GridSize, M, Size, BlockSize, ColComm);
	}
	void initialDistribution(double* AMatrix, double* BMatrix, double* CMatrix, int Size, int BlockSize) {
		
	}
}