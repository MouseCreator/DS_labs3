#include "fox.h"
#include "tape.h"
#include "cannon.h"

void initMPI(int argc, char* argv[]) {
	setvbuf(stdout, 0, _IONBF, 0);
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
	MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
}
void serialCalculator(double* pAMatrix, double* pBMatrix,
	double* pCMatrix, int Size) {
	int i, j, k;
	for (i = 0; i < Size; ++i) {
		int p = i * Size;
		for (j = 0; j < Size; ++j) {
			int v = p + j;
			for (k = 0; k < Size; ++k)
				pCMatrix[v] += pAMatrix[p + k] * pBMatrix[k * Size + j];
		}
	}
}
double calculateSerial(int size) {
	double* pAMatrix = new double[size * size];
	double* pBMatrix = new double[size * size];
	double* pCMatrix = new double[size * size];
	Matrices::random(pAMatrix, size);
	Matrices::random(pBMatrix, size);
	double begin = MPI_Wtime();
	serialCalculator(pAMatrix, pBMatrix, pCMatrix, size);
	double end = MPI_Wtime();
	delete[] pAMatrix;
	delete[] pBMatrix;
	delete[] pCMatrix;
	return end - begin;
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
		Cannon::runCannonMultiplication(argc, argv, dim);
		break;
	default:
		std::cout << "Unknown algorithm" << std::endl;
		break;
	}
}
void measure(char AlgId, int argc, char* argv[]) {
	int dims[5];
	if (ProcNum == 4) {
		dims[0] = 128;
		dims[1] = 256;
		dims[2] = 512;
		dims[3] = 1024;
		dims[4] = 2048;
	}
	else if (ProcNum == 9) {
		dims[0] = 99;
		dims[1] = 306;
		dims[2] = 603;
		dims[3] = 900;
		dims[4] = 1503;
	}
	else {
		if (ProcRank == 0)
			printf("Invalid proc num");
		return;
	}

	switch (AlgId)
	{
	case 'F':
		for (int i = 0; i < 5; i++) {
			int d = dims[i];
			double mult = Fox::runFoxMultiplication(argc, argv, d);
			if (ProcRank == 0) {
				double one = calculateSerial(d);
				double speedup = one / mult;
				printf("Seial Time: %7.4f\nSpeedup: %7.4f\n", one, speedup);
			}
			MPI_Barrier(MPI_COMM_WORLD);
		}
		break;
	case 'T':
		for (int i = 0; i < 5; i++) {
			int d = dims[i];
			double mult = Tape::runTapeMultiplication(argc, argv, d);
			if (ProcRank == 0) {
				double one = calculateSerial(d);
				double speedup = one / mult;
				printf("Seial Time: %7.4f\nSpeedup: %7.4f\n", one, speedup);
			}
			MPI_Barrier(MPI_COMM_WORLD);
		}
		break;
	case 'C':
		for (int i = 0; i < 5; i++) {
			int d = dims[i];
			double mult = Cannon::runCannonMultiplication(argc, argv, d);
			if (ProcRank == 0) {
				double one = calculateSerial(d);
				double speedup = one / mult;
				printf("Seial Time: %7.4f\nSpeedup: %7.4f\n", one, speedup);
			}
			MPI_Barrier(MPI_COMM_WORLD);
		}
		break;
	default:
		if (ProcRank == 0)
			printf("Unknown algorithm\n");
		break;
	}
}


void finalizeMPI() {
	MPI_Finalize();
}
void reveiceAndExecure(int argc, char* argv[]) {
	int dim;
	char command;
	char algorithm;
	if (ProcRank == 0) {
		printf("Enter command. C - calculate, M - measure.\nCommand: ");
		std::cin >> command;
	}
	MPI_Bcast(&command, 1, MPI_CHAR, 0, MPI_COMM_WORLD);
	if (ProcRank == 0) {
		printf("Enter algorithm. F - Fox, C - Cannon, T - Tape.\nAlgorithm: ");
		std::cin >> algorithm;
	}
	MPI_Bcast(&algorithm, 1, MPI_CHAR, 0, MPI_COMM_WORLD);

	if (command == 'C') {
		if (ProcRank == 0) {
			std::cout << "Dimensions: ";
			std::cin >> dim;
		}
		MPI_Bcast(&dim, 1, MPI_INT, 0, MPI_COMM_WORLD);
		calculate(algorithm, argc, argv, dim);
	}
	else if (command == 'M') {
		if (ProcRank == 0) {
			std::cout << "Measure\n";
		}
		measure(algorithm, argc, argv);
	}
	else {
		if (ProcRank == 0) {
			std::cout << "Unknown command!";
		}
	}
}
void main(int argc, char* argv[]) {
	initMPI(argc, argv);
	reveiceAndExecure(argc, argv);
	finalizeMPI();
}

