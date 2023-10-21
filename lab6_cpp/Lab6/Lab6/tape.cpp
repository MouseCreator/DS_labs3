#include <iostream>
#include <mpi.h>

int TapeSize;
int Coordninate;
int ProcNum = 0;
int ProcRank = 0;
MPI_Comm GridComm;
MPI_Comm ColComm;
MPI_Comm RowComm; 
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
	if (Coordninate == TapeSize - 1) NextProc = 0;
	int PrevProc = Coordninate - 1;
	if (Coordninate == 0) PrevProc = TapeSize - 1;
	MPI_Sendrecv_replace(matrixBTape, tapeLen * size, MPI_DOUBLE,
		NextProc, 0, PrevProc, 0, ColComm, &Status);
}

void calculateTape(double* matrixATape, double* matrixBTape, double* matrixCTape, int tapeLen, int size) {
	int iter = Coordninate;
	for (int i = 0; i < tapeLen; i++) {
		tapeMultiply(matrixATape, matrixBTape, matrixCTape, size, tapeLen, iter);
		iter++;
		if (iter == TapeSize) {
			iter = 0;
		}
		shiftB(matrixBTape, tapeLen, size);
	}
}

void sendInput() {

}

void sendResult() {

}