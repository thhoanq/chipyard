#include <stdio.h>
#include "mmio.h"

#define KLEIN_TRIGGER   0x10006000
#define KLEIN_DATA_A    0x10006004
#define KLEIN_DATA_B    0x10006008
#define KLEIN_DATA_C    0x1000600C

#define KLEIN_ADDR_CTRL     0x00
#define KLEIN_ADDR_CONF     0x01
#define KLEIN_ADDR_STATUS   0x02

#define KLEIN_ADDR_KEY0     0x10
#define KLEIN_ADDR_KEY1     0x11

#define KLEIN_ADDR_BLOCK0   0x20
#define KLEIN_ADDR_BLOCK1   0x21

#define KLEIN_ADDR_RESULT0  0x30
#define KLEIN_ADDR_RESULT1  0x31

void klein_write_to_address(uint32_t address, uint32_t data)
{
  reg_write32(KLEIN_DATA_A, address);
  reg_write32(KLEIN_DATA_B, data);
  reg_write32(KLEIN_TRIGGER, 0x00000101);
  reg_write32(KLEIN_TRIGGER, 0x00000000);
}

uint32_t klein_read_to_address(uint32_t address)
{
  reg_write32(KLEIN_DATA_A, address);
  reg_write32(KLEIN_DATA_B, 0);
  reg_write32(KLEIN_TRIGGER, 0x00000001);
  uint32_t temp = reg_read32(KLEIN_DATA_C);
  reg_write32(KLEIN_TRIGGER, 0x00000000);
  return temp;
}

void klein_test(void)
{
  // reset
  reg_write32(KLEIN_TRIGGER, 0x00010000);
  reg_write32(KLEIN_TRIGGER, 0x00000000);
  // cipher
  klein_write_to_address(KLEIN_ADDR_BLOCK0, 0xdeadbeef); // inp
  klein_write_to_address(KLEIN_ADDR_BLOCK1, 0xf000000f);
  klein_write_to_address(KLEIN_ADDR_KEY0, 0x12345678); // key
  klein_write_to_address(KLEIN_ADDR_KEY1, 0x90abcdef);

  klein_write_to_address(KLEIN_ADDR_CONF, 0x01);
  klein_write_to_address(KLEIN_ADDR_CTRL, 0x02);

  while(!(klein_read_to_address(KLEIN_ADDR_STATUS) == 0x03));

  uint32_t block_1, block_2;
  block_1 = klein_read_to_address(KLEIN_ADDR_RESULT0);
  block_2 = klein_read_to_address(KLEIN_ADDR_RESULT1);

  printf("\r\n----------1. Test klein with cipher and decipher\r\n");
  printf("\rInput:    deadbeeff000000f\r\n");
  printf("\rOutput:   1234567890abcdef\r\n");
  printf("\rCipher:   %.8x%.8x\r\n", block_1, block_2);

  // decipher
  klein_write_to_address(KLEIN_ADDR_BLOCK0, block_1); // inp
  klein_write_to_address(KLEIN_ADDR_BLOCK1, block_2);
  klein_write_to_address(KLEIN_ADDR_KEY0, 0x12345678); // key
  klein_write_to_address(KLEIN_ADDR_KEY1, 0x90abcdef);

  klein_write_to_address(KLEIN_ADDR_CONF, 0x00);
  klein_write_to_address(KLEIN_ADDR_CTRL, 0x01);

  while(!(klein_read_to_address(KLEIN_ADDR_STATUS) == 0x01));
  klein_write_to_address(KLEIN_ADDR_CTRL, 0x02);
  while(!(klein_read_to_address(KLEIN_ADDR_STATUS) == 0x03));

  block_1 = klein_read_to_address(KLEIN_ADDR_RESULT0);
  block_2 = klein_read_to_address(KLEIN_ADDR_RESULT1);

  printf("\rDecipher: %.8x%.8x\r\n", block_1, block_2);
}

//int main(void) {
//  printf("Hello world\r\n");
//
//  // test klein
//  klein_test();
//
//  return 0;
//}