#!/bin/sh

BASEURL="ftp://ftp.fu-berlin.de/pub/misc/movies/database/"
FILES="actors.list actresses.list directors.list locations.list release-dates.list plot.list"
PROCESSOR=wget

for i in $FILES
do
	if [ ! -e $i ]
	then
		$PROCESSOR $BASEURL$i.gz
	fi
done

exit 0
