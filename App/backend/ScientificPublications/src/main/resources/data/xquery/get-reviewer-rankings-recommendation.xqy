xquery version "3.1";

declare namespace sp = "http://www.ftn.uns.ac.rs/scientific-publication";
declare namespace cmn = "http://www.ftn.uns.ac.rs/common";

let $keywords := %s
let $owner := "$s"
let $documentTitle := "$s"
let $results := map {}



for $doc in collection("/db/scientific-publication/scientific-papers")
    let $authors := $doc/sp:scientific-paper/sp:header/sp:authors
    for $author in $authors/cmn:author/cmn:email
        (:where not($owners = $author):)
            let $trimedKeywords := replace($doc/sp:scientific-paper/sp:abstract/sp:keywords, " ", "")
            let $tokens := tokenize($trimedKeywords, ',')
            let $intersection := distinct-values($keywords[.=$tokens])
            let $score := count($intersection)
return concat($author, ',', $score)