#include <stdio.h>
#include "mmio.h"

#include "klein.h"
#include "blake2s.h"

#define ROM_ADDR 0x10005000
#define ROM_DOUT 0x10005004

int main(void) {
//  uint32_t result;
  printf("Hello world\r\n");

//  // test rom 8x32
//  printf("\r\n----------0. Test ROM 8x32:\r\n");
//
//  uint8_t i;
//  for(i = 0; i < 8; i++) {
//      reg_write32(ROM_ADDR, i);
//      result = reg_read32(ROM_DOUT);
//      printf("Address: 0x%d, Data: 0x%.8x\r\n", i, result);
//  }

  // test klein
  klein_test();

  // test blake2s
  blake2s_test_empty_message();
  blake2s_test_RFC_7693();

  return 0;
}