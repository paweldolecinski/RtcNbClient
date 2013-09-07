import os
import re

path = './libs'
p=path
#p = os.path.realpath(path)
print p

listing = os.listdir(path)
for file in listing:
 filePath = p + '/' + file
 m = re.search('^(.+)-(.+).jar$', file)
 artifactId = m.group(1)
 version = m.group(2)
 command = 'mvn install:install-file -Dfile='+filePath +' -DgroupId=pl.edu.amu.wmi.kino.rtc.libs -DartifactId='+artifactId +' -Dversion='+version +' -Dpackaging=jar -DgeneratePom=true'
 print 'Instaling', artifactId, '\nPath: ',	filePath
 os.popen(command)
 print 'Done.\n'
