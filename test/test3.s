program is
declare
    a, aux: int;
    b: float;
begin
    b = 0;
    in <<a;
    in<<b;
    if (a>b) then
        aux = b;
        b = a;
        a = aux
    end;
    out >> a;
    out >> b
end