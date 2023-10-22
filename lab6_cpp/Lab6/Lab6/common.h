#pragma once
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <mpi.h>
#include <iostream>

static int TapeSize;
static int ProcNum = 0;
static int ProcRank = 0;
static MPI_Comm ColComm;
static MPI_Comm RowComm;