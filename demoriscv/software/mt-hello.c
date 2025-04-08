// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "include/platform.h"
#include "kprintf.h"

//static int n_cores = 4;

#define REG32(p, i)	((p)[(i) >> 2])

int main(int argc, char **arv) {
  REG32(uart, UART_REG_TXCTRL) = UART_TXEN; // Already in bootROM

  kprintf("Hello world !!!.\r\n");

  uint32_t mhartid = read_csr(mhartid);
  kprintf("Hello world from core %c!!!\r\n", mhartid + 48);
  return 0;
}
