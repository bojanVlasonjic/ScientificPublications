xquery version "3.0";
for $doc in collection()
return util:document-name($doc)