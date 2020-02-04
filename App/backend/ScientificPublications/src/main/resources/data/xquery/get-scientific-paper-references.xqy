xquery version "3.0";

declare namespace sp = "http://www.ftn.uns.ac.rs/scientific-publication";
for $ref in doc("%s")/sp:scientific-paper/sp:references/sp:reference
let $href := data($ref/@href)
return $href