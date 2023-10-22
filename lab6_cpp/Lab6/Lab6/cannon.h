#pragma once
#include "common.h"
namespace Cannon {

	int GridCoords[2];
	int GridSize;
	MPI_Comm GridComm;
	void multiplyBlocks(double* pAMatrix, double* pBMatrix,
		double* pCMatrix, int Size) {
		int i, j, k;
		for (i = 0; i < Size; i++) {
			for (j = 0; j < Size; j++)
				for (k = 0; k < Size; k++)
					pCMatrix[i * Size + j] += pAMatrix[i * Size + k] * pBMatrix[k * Size + j];
		}
	}

	void shiftA(double* ABlock, int Size, int BlockSize) {
		MPI_Status Status;
		int NextProc = GridCoords[1] + 1;
		if (GridCoords[1] == GridSize - 1) NextProc = 0;
		int PrevProc = GridCoords[1] - 1;
		if (GridCoords[1] == 0) PrevProc = GridSize - 1;
		MPI_Sendrecv_replace(ABlock, BlockSize * BlockSize, MPI_DOUBLE,
			NextProc, 0, PrevProc, 0, RowComm, &Status);
	}
	void shiftB(double* BBlock, int Size, int BlockSize) {
		MPI_Status Status;
		int NextProc = GridCoords[0] + 1;
		if (GridCoords[0] == GridSize - 1) NextProc = 0;
		int PrevProc = GridCoords[0] - 1;
		if (GridCoords[0] == 0) PrevProc = GridSize - 1;
		MPI_Sendrecv_replace(BBlock, BlockSize * BlockSize, MPI_DOUBLE,
			NextProc, 0, PrevProc, 0, ColComm, &Status);
	}

	void collectResult(double* pCMatrix, double* pCblock, int Size,
		int BlockSize) {
		double* pResultRow = new double[Size * BlockSize];
		for (int i = 0; i < BlockSize; i++) {
			MPI_Gather(&pCblock[i * BlockSize], BlockSize, MPI_DOUBLE,
				&pResultRow[i * Size], BlockSize, MPI_DOUBLE, 0, RowComm);
		}
		if (GridCoords[1] == 0) {
			MPI_Gather(pResultRow, BlockSize * Size, MPI_DOUBLE, pCMatrix,
				BlockSize * Size, MPI_DOUBLE, 0, ColComm);
		}
		delete[] pResultRow;
	}
	void calculate(double* ABlock, double* BBlock, double* CBlock, int Size, int BlockSize) {
		for (int i = 0; i < GridSize; ++i) {
			multiplyBlocks(ABlock, BBlock, CBlock, BlockSize);
			shiftA(ABlock, Size, BlockSize);
			shiftB(BBlock, Size, BlockSize);
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

	void terminate(double* pAMatrix, double* pBMatrix,
		double* pCMatrix, double* pAblock, double* pBblock, double* pCblock) {
		if (ProcRank == 0) {
			delete[] pAMatrix;
			delete[] pBMatrix;
			delete[] pCMatrix;
		}
		delete[] pAblock;
		delete[] pBblock;
		delete[] pCblock;
	}
	void ones(double* A, double* B, int Size) {
		int sq = Size * Size;
		for (int i = 0; i < sq; ++i) {
			A[i] = B[i] = 1;
		}
	}
	void initialize(double*& pAMatrix, double*& pBMatrix,
		double*& pCMatrix, double*& pAblock, double*& pBblock, double*& pCblock, int& Size, int& BlockSize) {

		BlockSize = Size / GridSize;
		pAblock = new double[BlockSize * BlockSize];
		pBblock = new double[BlockSize * BlockSize];
		pCblock = new double[BlockSize * BlockSize];
		for (int i = 0; i < BlockSize * BlockSize; i++) {
			pCblock[i] = 0;
		}
		if (ProcRank == 0) {
			pAMatrix = new double[Size * Size];
			pBMatrix = new double[Size * Size];
			pCMatrix = new double[Size * Size];
			ones(pAMatrix, pBMatrix, Size);
		}
	}
	void print(double* pMatrix, int size) {
		int i, j;
		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++)
				printf("%7.4f ", pMatrix[i * size + j]);
			printf("\n");
		}
	}

	void runCannonMultiplication(int argc, char* argv[], int dim) {
		double* pAMatrix;
		double* pBMatrix; 
		double* pCMatrix;
		int Size;
		int BlockSize;
		double* pAblock;
		double* pBblock; 
		double* pCblock; 
		double Start, Finish, Duration;
		Size = dim;
		GridSize = sqrt((double)ProcNum);
		if (ProcNum != GridSize * GridSize) {
			if (ProcRank == 0) {
				printf("Number of processes must be a perfect square \n");
			}
			return;
		}
		CreateGridCommunicators();
		initialize(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock,
			pCblock, Size, BlockSize);
		distribute(pAMatrix, pAblock, pBMatrix, pBblock, Size, BlockSize);
		calculate(pAblock, pBblock, pCblock, Size, BlockSize);
		collectResult(pCMatrix, pCblock, Size, BlockSize);
		print(pCMatrix, Size);
		terminate(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock,
			pCblock);
		
	}
}