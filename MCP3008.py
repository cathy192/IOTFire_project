from spidev import SpiDev

class MCP3008:
    def __init__(self, bus = 0, device = 0):
        self.bus, self.device = bus, device
        self.spi = SpiDev()
        self.open()
        self.spi1 = SpiDev()
        self.open1()
        self.spi.max_speed_hz = 1000000 # 1MHz

    def open(self):
        self.spi.open(self.bus, self.device)
        self.spi.max_speed_hz = 1000000 # 1MHz
    def open1(self):
        self.spi1.open(0,1)
        self.spi1.max_speed_hz=1000000
    def read(self, channel):
        #if channel==0:
            cmd1 = 4 | 2 | (( channel & 4) >> 2)
        #print("cmd1:%d"%cmd1)
            cmd2 = (channel & 3) << 6
        #print("cmd2:%d"%cmd2)
        #data=0
        #adc=self.spi.xfer2([1,(8+channel)<<4,0])
        #data=((adc[1]&3)<<8)+adc[2]
            adc = self.spi.xfer2([cmd1, cmd2, 0])
        #print("raw data:")
        #print(adc)
            data = ((adc[1] & 15) << 8) + adc[2]
       # elif channel==2:
           # cmd=self.spi1.xfer2([1,8+channel<<4,0])
           # data=((cmd[1]&3)<<8)+cmd[2]
            return data
    def close(self):
        self.spi.close()
