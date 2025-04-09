// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "include/platform.h"
#include "kprintf.h"

#define REG32(p, i)	((p)[(i) >> 2])
#define DELAY_TIME 1000000

static volatile uint32_t *msip = (uint32_t *)(CLINT_CTRL_ADDR);

void delay() {
	for (int i = 0; i < DELAY_TIME; i = i + 1);
  for (int i = 0; i < DELAY_TIME; i = i + 1);
}

int main(int hartid, char **arv) {
  REG32(uart, UART_REG_TXCTRL) = UART_TXEN; // Already in bootROM

  uint32_t core_id = read_csr(mhartid);

  if(core_id == 0) {
    kprintf("Hello from core %c\r\n", core_id + 48);
    delay();
    REG32(msip, CLINT_MSIP0) = CLINT_MSIPCLR;
    REG32(msip, CLINT_MSIP1) = CLINT_MSIPEN;
  }
  if(core_id == 1) {
    kprintf("Hello from core %c\r\n", core_id + 48);
    delay();
    REG32(msip, CLINT_MSIP1) = CLINT_MSIPCLR;
    REG32(msip, CLINT_MSIP2) = CLINT_MSIPEN;
  }
  if(core_id == 2) {
    kprintf("Hello from core %c\r\n", core_id + 48);
    delay();
    REG32(msip, CLINT_MSIP2) = CLINT_MSIPCLR;
    REG32(msip, CLINT_MSIP3) = CLINT_MSIPEN;
  }
  if(core_id == 3) {
    kprintf("Hello from core %c\r\n", core_id + 48);
    delay();
    REG32(msip, CLINT_MSIP3) = CLINT_MSIPCLR;
    REG32(msip, CLINT_MSIP0) = CLINT_MSIPEN;
  }

  return 0;
}
