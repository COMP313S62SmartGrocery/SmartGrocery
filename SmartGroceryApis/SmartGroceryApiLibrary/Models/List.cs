using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SmartGroceryApiLibrary.Models
{
    public class List
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Color { get; set; }
        public string LastModified { get; set; }
        public string Username { get; set; }
    }
}
