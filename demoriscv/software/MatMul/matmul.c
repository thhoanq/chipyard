// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "platform.h"
#include "kprintf.h"
#include "thread.h"

#define REG32(p, i)	((p)[(i) >> 2])
#define DELAY_TIME 1000000

void delay() {
	for (int i = 0; i < DELAY_TIME; i = i + 1);
  for (int i = 0; i < DELAY_TIME; i = i + 1);
}

static uint32_t flag = 0;

int main(int hartid, char **arv) {

  if(hartid == 0) {
    kprintf("Hello from core %c\r\n", hartid + 48);
    kprintf("Value of flag: %c\r\n", flag + 48);
    delay();
//    atomic_write(&flag, 1);
    REG32(msip, CLINT_MSIP0) = CLINT_MSIPCLR;
    REG32(msip, CLINT_MSIP1) = CLINT_MSIPEN;
  }
  if(hartid == 1) {
    kprintf("Hello from core %c\r\n", hartid + 48);
    kprintf("Value of flag: %c\r\n", flag + 48);
    delay();
    REG32(msip, CLINT_MSIP1) = CLINT_MSIPCLR;
    REG32(msip, CLINT_MSIP0) = CLINT_MSIPEN;
  }

  return 0;
}
