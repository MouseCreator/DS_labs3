
#pragma once

#include "common.h"
namespace Tape {
	int Coordninate;

	void tapeMultiply(double* aTape, double* bTape, double* res, int size, int tapeLen, int iter) {
		int index = tapeLen * iter;
		for (int i = 0; i < tapeLen; i++, index += size - tapeLen) {
			for (int j = 0; j < tapeLen; j++, index++) {
				double result = 0;
				for (int k = 0; k < size; k++) {
					result += aTape[i * tapeLen + k] * bTape[j * tapeLen + k];
				}
				res[index] = result;
			}
		}
	}

	void shiftB(double* matrixBTape, int tapeLen, int size) {
		MPI_Status Status;
		int NextProc = Coordninate + 1;
		if (NextProc == ProcNum) NextProc = 0;
		int PrevProc = Coordninate - 1;
		if (NextProc == -1) PrevProc = ProcNum - 1;
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
		for (int i = 0; i < tapeLen; i++) {
			tapeMultiply(matrixATape, matrixBTape, matrixCTape, size, tapeLen, iter);
			iter++;
			if (iter == tapeLen) {
				iter = 0;
			}
			shiftB(matrixBTape, tapeLen, size);
		}
	}

	void sendMatrixTape(double* matrix, double* matrixTape, int tapeLen, int size) {
		MPI_Scatter(&(matrix[size * tapeLen * Coordninate]), tapeLen * size, MPI_DOUBLE,
			matrixTape, tapeLen * size, MPI_DOUBLE, 0, RowComm);
	}

	void sendResult(double* cMatrix, double* cMatrixTape, int tapeLen, int size) {
		MPI_Gather(cMatrixTape, tapeLen * size, MPI_DOUBLE,
			cMatrix, tapeLen * size, MPI_DOUBLE, 0, RowComm);
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

		int tapeSize = tapeLen * Size;

		matrixATape = new double[tapeSize];
		matrixBTape = new double[tapeSize];
		matrixCTape = new double[tapeSize];
		for (int i = 0; i < tapeSize; i++) {
			matrixCTape[i] = 0;
		}
		if (ProcRank == 0) {
			pAMatrix = new double[Size * Size];
			pBMatrix = new double[Size * Size];
			pCMatrix = new double[Size * Size];
			initMatrix(pAMatrix, Size, 1);
			initMatrix(pBMatrix, Size, 1);
			initMatrix(pCMatrix, Size, 0);
		}

	}

	void transpose(double* pMatrix, int Size) {
		int* transpose = new int[Size * Size];

		for (int i = 0; i < Size; ++i) {
			for (int j = 0; j < Size; ++j) {
				transpose[j * Size + i] = pMatrix[i * Size + j];
			}
		}

		for (int i = 0; i < Size * Size; ++i) {
			pMatrix[i] = transpose[i];
		}

		delete[] transpose;
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

	void print(double* pMatrix, int size) {
		int i, j;
		for (i = 0; i < size; i++) {
			for (j = 0; j < size; j++)
				printf("%7.4f ", pMatrix[i * size + j]);
			printf("\n");
		}
	}

	void terminate(double* Amatrix, double* Bmatrix, double* Cmatrix, double* aTape, double* bTape, double* cTape) {
		if (ProcRank == 0) {
			delete[] Amatrix;
			delete[] Bmatrix;
			delete[] Cmatrix;
		}
		delete[] aTape;
		delete[] bTape;
		delete[] cTape;
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
		initProcess(pAMatrix, pBMatrix, pCMatrix, pATape, pBTape, pCTape, Size, TapeLen);
		createTapeCommunicators(TapeLen);
		unifyAndDistribute(pAMatrix, pBMatrix, pATape, pBTape, Size, TapeLen);
		Start = MPI_Wtime();
		calculateTape(pATape, pBTape, pCTape, TapeLen, Size);
		Finish = MPI_Wtime();
		collectResult(pCMatrix, pCTape, TapeLen, Size);
		terminate(pAMatrix, pBMatrix, pCMatrix, pATape, pBTape, pCTape);
		Duration = Finish - Start;
		if (ProcRank == 0)
		printf("Tape Algorithm[%dx%d]: %7.4f\n", Size, Size, Duration);
		return Duration;
	}
}

