#pragma once
#include "common.h"
namespace Cannon {

	int GridCoords[2];
	int GridSize;
	MPI_Comm GridComm;

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

	void shiftA() {

	}
	void shiftB() {

	}

	void CreateGridCommunicators() {
		int DimSize[2];
		int Periodic[2];
		int Subdims[2]; 
		DimSize[0] = GridSize;
		DimSize[1] = GridSize;
		Periodic[0] = 0;
		Periodic[1] = 0;
		MPI_Cart_create(MPI_COMM_WORLD, 2, DimSize, Periodic, 1, &GridComm);
		MPI_Cart_coords(GridComm, ProcRank, 2, GridCoords);
		Subdims[0] = 0; 
		Subdims[1] = 1; 
		MPI_Cart_sub(GridComm, Subdims, &RowComm);
		Subdims[0] = 1;
		Subdims[1] = 0;
		MPI_Cart_sub(GridComm, Subdims, &ColComm);
	}
}