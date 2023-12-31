
#pragma once

#include "common.h"
namespace Tape {
	int Coordninate;

	void tapeMultiply(double* aTape, double* bTape, double* res, int size, int tapeLen, int iter) {
		int index = tapeLen * iter;

		for (int i = 0; i < tapeLen; i++) {
			for (int j = 0; j < tapeLen; j++) {

				for (int k = 0; k < size; k++) {
					res[index] += aTape[i * size + k] * bTape[j * size + k];
				}
				index++;
			}
			index += size - tapeLen;
		}
	}

	void print(double* pMatrix, int size) {
		int i, j;
		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++)
				printf("%7.4f ", pMatrix[i * size + j]);
			printf("\n");
		}
		printf("\n");
	}
	void shiftB(double* matrixBTape, int tapeLen, int size) {
		MPI_Status Status;
		int NextProc = Coordninate + 1;
		if (NextProc == ProcNum) NextProc = 0;
		int PrevProc = Coordninate - 1;
		if (PrevProc == -1) PrevProc = ProcNum - 1;
		MPI_Sendrecv_replace(matrixBTape, tapeLen * size, MPI_DOUBLE,
			NextProc, 0, PrevProc, 0, ColComm, &Status);
	}
	void printTape(double* tape, int size, int tapeLen) {
		for (int i = 0; i < tapeLen; i++) {
			for (int j = 0; j < size; j++)
				printf("%7.4f ", tape[i * size + j]);
			printf("\n");
		}
		printf("\n");
	}
	void calculateTape(double* matrixATape, double* matrixBTape, double* matrixCTape, int tapeLen, int size) {
		int iter = Coordninate;
		for (int i = 0; i < ProcNum; i++) {
			tapeMultiply(matrixATape, matrixBTape, matrixCTape, size, tapeLen, iter);
			iter++;
			if (iter == ProcNum) {
				iter = 0;
			}
			shiftB(matrixBTape, tapeLen, size);
		}
	}

	void sendMatrixTape(double* matrix, double* matrixTape, int tapeLen, int size) {
		MPI_Scatter(&(matrix[size * tapeLen * Coordninate]), tapeLen * size, MPI_DOUBLE, matrixTape, tapeLen * size, MPI_DOUBLE, 0, RowComm);
		
	}

	void sendResult(double* cMatrix, double* cMatrixTape, int tapeLen, int size) {
		MPI_Gather(cMatrixTape, tapeLen * size, MPI_DOUBLE,cMatrix, tapeLen * size, MPI_DOUBLE, 0, RowComm);
		
	}
	void createTapeCommunicators(int TapeLen) {
		MPI_Comm_split(MPI_COMM_WORLD, ProcRank / TapeLen, ProcRank, &RowComm);
		MPI_Comm_split(MPI_COMM_WORLD, ProcRank / TapeLen, ProcRank, &ColComm);
	}

	void initMatrix(double* pAMatrix, int Size, int value) {
		int sizeSq = Size * Size;
		for (int i = 0; i < sizeSq; ++i) {
			pAMatrix[i] = value;
		}
	}
	// Function for memory allocation and data initialization
	void initProcess(double*& pAMatrix, double*& pBMatrix,
		double*& pCMatrix, double*& matrixATape, double*& matrixBTape, double*& matrixCTape,
		int& Size, int& tapeLen) {
		tapeLen = Size / ProcNum;

		matrixATape = new double[tapeLen * Size];
		matrixBTape = new double[tapeLen * Size];
		matrixCTape = new double[tapeLen * Size];
		for (int i = 0; i < tapeLen * Size; i++) {
			matrixCTape[i] = 0;
		}
		if (ProcRank == 0) {
			pAMatrix = new double[Size * Size];
			pBMatrix = new double[Size * Size];
			pCMatrix = new double[Size * Size];
			Matrices::random(pAMatrix, Size);
			Matrices::random(pBMatrix, Size);
			Matrices::zeros(pCMatrix, Size);
		}

	}

	void transpose(double* pMatrix, int Size) {
		for (int i = 0; i < Size; i++) {
			for (int j = 0; j < Size; j++) {
				float tmp = pMatrix[i * Size + j];
				pMatrix[i * Size + j] = pMatrix[j * Size + i];
				pMatrix[j * Size + i] = tmp;
			}
		}
	}

	void unifyAndDistribute(double* pAMatrix, double* pBMatrix, double* pATape, double* pBTape, int Size, int TapeLen) {
		if (ProcRank == 0) {
			transpose(pBMatrix, Size);
		}
		sendMatrixTape(pAMatrix, pATape, TapeLen, Size);
		sendMatrixTape(pBMatrix, pBTape, TapeLen, Size);
	}

	void collectResult(double* cMatrix, double* cMatrixTape, int tapeLen, int size) {
		sendResult(cMatrix, cMatrixTape, tapeLen, size);
	}


	void terminate(double* Amatrix, double* Bmatrix, double* Cmatrix, double* aTape, double* bTape, double* cTape) {
		delete[] aTape;
		delete[] bTape;
		delete[] cTape;
		if (ProcRank == 0) {
			delete[] Amatrix;
			delete[] Bmatrix;
			delete[] Cmatrix;
		}
	}

	double runTapeMultiplication(int argc, char* argv[], int dim) {
		double* pAMatrix;
		double* pBMatrix;
		double* pCMatrix;
		int Size;
		int TapeLen;
		double* pATape;
		double* pBTape;
		double* pCTape;
		double Start, Finish, Duration;
		Coordninate = ProcRank;
		Size = dim;
		if (dim % ProcNum != 0) {
			if (ProcRank == 0) printf("Dimensions Error!");
			return 1;
		}
		initProcess(pAMatrix, pBMatrix, pCMatrix, pATape, pBTape, pCTape, Size, TapeLen);
		createTapeCommunicators(TapeLen);

		Start = MPI_Wtime();
		unifyAndDistribute(pAMatrix, pBMatrix, pATape, pBTape, Size, TapeLen);
		calculateTape(pATape, pBTape, pCTape, TapeLen, Size);
		Finish = MPI_Wtime();
		collectResult(pCMatrix, pCTape, TapeLen, Size);
		terminate(pAMatrix, pBMatrix, pCMatrix, pATape, pBTape, pCTape);
		Duration = Finish - Start;
		if (ProcRank == 0) {
			printf("Tape Algorithm[%dx%d]: %7.4f\n", Size, Size, Duration);
		}
		return Duration;
	}
}

