xquery version "3.1";

declare namespace sp = "http://www.ftn.uns.ac.rs/scientific-publication";
declare namespace cmn = "http://www.ftn.uns.ac.rs/common";

let $requestedAuthor := '%s'

for $doc in collection("/db/scientific-publication/scientific-papers")
    let $authors := $doc/sp:scientific-paper/sp:header/sp:authors
    for $author in $authors/cmn:author/cmn:email
        where $requestedAuthor = $author
        let $keywords := $doc/sp:scientific-paper/sp:abstract/sp:keywords
        return string($keywords)