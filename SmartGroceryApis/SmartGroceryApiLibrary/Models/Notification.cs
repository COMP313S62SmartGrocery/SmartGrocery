using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SmartGroceryApiLibrary.Models
{
    public class Notification
    {
        public long Id { get; set; }
        public string From { get; set; }
        public string Subject { get; set; } 
        public string Text { get; set; }
        public string Date { get; set; }
        public bool IsRead { get; set; }
        public string Username { get; set; }
    }
}
