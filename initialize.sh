#!/bin/bash
filename='/usr/share/nginx/html/index.html'
n=1
found_line=0
input_line="<input id=\"lifehacker_url\" type=\"hidden\">"
url=$LIFEHACKER_URL
paste_line="<input id=\"lifehacker_url\" type=\"hidden\" value=\"$url\">"
html_content=""

while read line; do
    if [ "$line" = "$input_line" ]; then
        found_line=$n
    fi
    n=$((n+1))
done < $filename

n=1
while read line; do
    if [ $n = $found_line ]; then
        html_content="$html_content$paste_line"
    elif [ $n = 1 ]; then
        html_content="$line"
    else
        html_content="$html_content$line"
    fi
    n=$((n+1))
done < $filename

rm $filename
echo $html_content > $filename

service nginx restart