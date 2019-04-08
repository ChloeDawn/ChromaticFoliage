import os
import re

for n in [f for f in os.listdir('.') if re.match(r'(.*)', f)]:
    with open(n, 'r+') as i:
        f = i.read().replace('minecraft:item/', 'minecraft:item/')
        with open(n, 'w') as o:
            o.write(f)
