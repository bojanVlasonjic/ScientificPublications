xquery version "3.1";

declare namespace sp = "http://www.ftn.uns.ac.rs/scientific-publication";
declare namespace cmn = "http://www.ftn.uns.ac.rs/common";

let $requestedPaperTitle := '%s'

for $doc in collection("/db/scientific-publication/scientific-papers")
    let $authors := $doc/sp:scientific-paper/sp:header/sp:authors
    for $author in $authors/cmn:author/cmn:email
        let $title := $doc/sp:scientific-paper/sp:header/sp:title
        where $title = $requestedPaperTitle
        return string($author)