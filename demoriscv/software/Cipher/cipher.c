// #include <stdio.h>
#include <stdint.h>
#include <riscv-pk/encoding.h>
#include "platform.h"
#include "kprintf.h"

#define REG32(p, i)	((p)[(i) >> 2])

int main(int hartid, char **arv) {

  if(hartid == 0)
    kprintf("Hello world!\r\n");

  return 0;
}
