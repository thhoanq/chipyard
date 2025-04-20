#################################
# RISCV Toolchain
#################################

ROOT_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
BUILD_DIR := $(ROOT_DIR)build

CC=$(RISCV)/bin/riscv64-unknown-elf-gcc
OBJCOPY=$(RISCV)/bin/riscv64-unknown-elf-objcopy
OBJDUMP=$(RISCV)/bin/riscv64-unknown-elf-objdump

CFLAGS=-march=rv64gc -mcmodel=medany -O0 -std=gnu11 -Wall -nostartfiles
CFLAGS+= -fno-common -g -DENTROPY=0 -mabi=lp64d -DNONSMP_HART=0
CFLAGS+= -I $(ROOT_DIR)/include -I.

LFLAGS=-static -nostdlib -L $(ROOT_DIR)/linker -T matmul.elf.lds

# Clock option passed in
PBUS_CLK ?= 1000000


#################################
# Build matmul by default
#################################
.PHONY: default
default: matmul.bin matmul.dump


#################################
# MATMUL build rules
#################################
MATMUL_ELF  := $(BUILD_DIR)/matmul.elf
MATMUL_BIN  := $(BUILD_DIR)/matmul.bin
MATMUL_DUMP := $(BUILD_DIR)/matmul.dump

$(MATMUL_ELF): matmul.c kprintf.c
	mkdir -p $(BUILD_DIR)
	$(CC) $(CFLAGS) -DTL_CLK="$(PBUS_CLK)UL" $(LFLAGS) -o $@ crt0.S matmul.c kprintf.c thread.S

.PHONY: matmul.elf
matmul.elf: $(MATMUL_ELF)

$(MATMUL_BIN): $(MATMUL_ELF)
	mkdir -p $(BUILD_DIR)
	$(OBJCOPY) -O binary $< $@

.PHONY: matmul.bin
matmul.bin: $(MATMUL_BIN)

$(MATMUL_DUMP): $(MATMUL_ELF)
	$(OBJDUMP) -D -S $< > $@

.PHONY: matmul.dump
matmul.dump: $(MATMUL_DUMP)


#################################
# CLEAN
#################################
.PHONY: clean
clean:
	rm -rf $(BUILD_DIR)
