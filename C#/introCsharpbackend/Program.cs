Sales sale = new Sales(15);
sale.Total = 15;
Sales sale2 = new(26);
sale2.Total = 25;
var sale3 = new Sales(200);
sale3.Total = 45;

var message = sale3.GetInfo();
Console.WriteLine(message);

class SaleWithTax : Sales
{
    public decimal Tax { get; set; }
    public SaleWithTax(decimal Total) : base(Total) { 
    }
    public string GetInfoWithTax()
    {
        return "El total es: " +  Total + " Impuesto es:"+ Tax;
    }
    public override string GetInfo()
    {
        return "Sobreescirtura metodo desde el hijo, el total es: " + Total;
    }
    public string GetInfo(int a) 
    { 
    return a.ToString();
    }
}
class Sales
{
    public decimal Total { get; set; }
    private decimal _Some;
   
    
    public Sales(decimal Total) 
    { 
        this.Total = Total;
        _Some = 9;
    }
    public virtual string GetInfo() 
    {
     return "el total es: " + Total;
    }

}