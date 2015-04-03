using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.Models
{
    public class ListItem
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public float Quantity { get; set; }
        public string Unit { get; set; }
        public string LastModified { get; set; }
        public string Reminder { get; set; }
        public long ListId { get; set; }
    }
}
