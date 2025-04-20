#ifndef _ASM_H
#define _ASM_H

#include <stdint.h>
#include "platform.h"

static volatile uint32_t *msip = (uint32_t *)(CLINT_CTRL_ADDR);

extern void atomic_write(uint32_t *addr, unsigned int value);

#endif