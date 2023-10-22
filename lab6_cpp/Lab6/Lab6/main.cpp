#include "fox.h"
#include "tape.h"
#include "cannon.h"
//https://github.com/microsoft/Microsoft-MPI/blob/master/src/include/mpi.h

void initMPI(int argc, char* argv[]) {
	setvbuf(stdout, 0, _IONBF, 0);
	MPI_Init(&argc, &argv);
}


void calculate(char AlgId, int argc, char* argv[], int dim) {
	switch (AlgId)
	{
	case 'F':
		Fox::runFoxMultiplication(argc, argv, dim);
		break;
	case 'T':
		Tape::runTapeMultiplication(argc, argv, dim);
		break;
	case 'C':
		break;
	default:
		std::cout << "Unknown algorithm" << std::endl;
		break;
	}
}
void measure(char AlgId, int argc, char* argv[]) {
	int n;
	switch (AlgId)
	{
	case 'F':
		Fox::runFoxMultiplication(argc, argv, dim);
		break;
	case 'T':
		Tape::runTapeMultiplication(argc, argv, dim);
		break;
	case 'C':
		break;
	default:
		std::cout << "Unknown algorithm" << std::endl;
		break;
	}
}
void SerialResultCalculation(double* pAMatrix, double* pBMatrix,
	double* pCMatrix, int Size) {
	int i, j, k;
	for (i = 0; i < Size; i++) {
		for (j = 0; j < Size; j++)
			for (k = 0; k < Size; k++)
				pCMatrix[i * Size + j] += pAMatrix[i * Size + k] * pBMatrix[k * Size + j];
	}
}
void finalizeMPI() {
	MPI_Finalize();
}

void main(int argc, char* argv[]) {

	int dim;
	char command;
	char algorithm;
	initMPI(argc, argv);
	if (ProcRank == 0) {
		std::cout << "Command: ";
		std::cin >> command;
	}
	MPI_Bcast(&command, 1, MPI_CHAR, 0, MPI_COMM_WORLD);
	if (ProcRank == 0) {
		std::cout << "Algorithm: ";
		std::cin >> algorithm;
	}
	MPI_Bcast(&algorithm, 1, MPI_CHAR, 0, MPI_COMM_WORLD);

	if (command == 'C') {
		if (ProcRank == 0) {
			std::cout << "Dimensions: ";
			std::cin >> dim;
		}
		MPI_Bcast(&dim, 1, MPI_CHAR, 0, MPI_COMM_WORLD);
		calculate(command, argc, argv, dim);
	}
	else if (command == 'M') {
		
	}
	else {
		if (ProcRank == 0) {
			std::cout << "Unknown command!";
		}
	}

	finalizeMPI();
	
}

