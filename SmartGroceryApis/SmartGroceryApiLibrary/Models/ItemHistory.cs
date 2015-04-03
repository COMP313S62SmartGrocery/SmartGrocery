using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.Models
{
    public class ItemHistory
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public float Quantity { get; set; }
        public string Unit { get; set; }
        public string Date { get; set; }
        public string Username { get; set; }
    }
}
