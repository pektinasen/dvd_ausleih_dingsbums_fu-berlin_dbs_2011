#!/bin/sh

IMDBURL="ftp://ftp.fu-berlin.de/pub/misc/movies/database/"
IMDBFILES="actors.list actresses.list directors.list locations.list release-dates.list plot.list"
ADDITIONAL_URL="http://student:dbms@www.inf.fu-berlin.de/lehre/SS11/DBS-Intro/"
ADDITIONAL_FILES="modmovies.list customers.list rentals.list"

PROCESSOR=wget

for i in $IMDBFILES
do
	echo -n "Downloading $i... "
	if [ ! -e $i ]
	then
		echo
		$PROCESSOR $IMDBURL$i.gz
		gzip -d $i.gz
	else
		echo "already there."
	fi
done

for j in $ADDITIONAL_FILES
do
	echo -n "Downloading $j... "
	if [ ! -e $j ]
	then
		echo
		$PROCESSOR $ADDITIONAL_URL$j
	else
		echo "already there."
	fi
done

exit 0
