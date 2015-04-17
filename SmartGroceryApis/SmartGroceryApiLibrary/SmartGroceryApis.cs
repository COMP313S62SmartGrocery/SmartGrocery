using SmartGroceryApiLibrary.DataAccess;
using SmartGroceryApiLibrary.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace SmartGroceryApiLibrary
{
    public class SmartGroceryApis : ISmartGroceryApis
    {
        #region user
        public string Register(User user)
        {
            if (!UserSet.IsExists(user.Username))
            {
                return UserSet.AddUser(user.Username, user.Password);
            }

            return "-1";
        }

        public string Authenticate(string authKey)
        {
            return UserSet.Authenticate(authKey);
        }

        public string GetAuthKey(User user)
        {
            return UserSet.GetAuthKey(user.Username, user.Password);
        }

        public bool UpdatePassword(User user, string newPassword)
        {
            return UserSet.UpdatePassword(user.Username, user.Password, newPassword);
        }

        public bool Delete(User user)
        {
            if (IsValidUser(user))
            {
                return UserSet.RemoveUser(user.Username, user.Password);
            }

            return false;

        }

        private bool IsValidUser(User user)
        {
            string username = UserSet.GetUsername(user.SESS_KEY);
            if (!string.IsNullOrEmpty(username) && username.Equals(user.Username, StringComparison.CurrentCulture))
            {
                return true;
            }

            return false;
        }
        #endregion

        #region template

        public List<Template> GetTemplates(string query)
        {
            return TemplateSet.GetTemplateList(query);
        }

        public Template GetTemplate(string templateId)
        {
            return TemplateSet.GetTemplate(int.Parse(templateId));
        }

        public List<TemplateItem> GetTemplateItems(string templateId)
        {
            return TemplateItemSet.GetTemplateItems(int.Parse(templateId));
        }
        #endregion

        #region notification

        public List<Notification> GetNotifications(User user)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.GetNotifications(user.Username);
            }
            return null;
        }

        public string GetNotificationsCount(User user)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.CountNotifications(user.Username).ToString();
            }

            return "-1";
        }

        public bool SetNotificationAsRead(User user, int notificationId)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.SetNotificationRead(new Notification() { Id = notificationId, Username = user.Username });
            }

            return false;
        }

        public bool UpdateNotification(User user, int notificationId, string text)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.UpdateNotification(user.Username, notificationId, text);
            }

            return false;
        }

        public bool DeleteNotification(User user, int notificationId)
        {
            if (IsValidUser(user))
            {
                return NotificationSet.DeleteNotification(new Notification() { Id = notificationId, Username = user.Username });
            }

            return false;
        }
        #endregion

        #region ItemHistory
        public List<string> GetItems(User user)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.GetItemList(user.Username);
            }
            return null;
        }

        public List<string> GetYears(User user, string itemName)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.GetItemHistoryYears(user, itemName);
            }
            return null;
        }

        public List<ItemHistory> GetItemHistory(User user, string itemName, string month, string year)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.GetItemHistory(itemName, user.Username, month, year);
            }
            return null;
        }

        public bool ClearItemHistory(User user, string itemName)
        {
            if (IsValidUser(user))
            {
                return ItemHistorySet.ClearHistory(itemName, user.Username);
            }
            return false;
        }

        #endregion

        #region List
        public List<List> GetLists(User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.GetLists(user.Username);
            }
            return null;
        }

        public string GetLastModified(User user, string listId)
        {
            if (IsValidUser(user))
            {
                return ListSet.GetLastModified(long.Parse(listId));
            }
            return "-1";
        }

        public string AddList(List list, User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.AddList(list).ToString();
            }
            return "-1";
        }

        public bool RenameList(long listId, string newName, User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.RenameList(listId, newName);
            }
            return false;
        }

        public bool ShareList(long listId, string withUsername, User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.ShareList(listId, withUsername);
            }
            return false;
        }

        public bool UnshareList(long listId, string withUsername, User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.UnShareList(listId, withUsername);
            }
            return false;
        }

        public List DuplicateList(long listId, string listName, User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.DuplicateList(listId, listName, user.Username);
            }
            return null;
        }

        public bool DeleteList(long listId, User user)
        {
            if (IsValidUser(user))
            {
                return ListSet.DeleteList(listId);
            }
            return false;
        }

        public string AddListFromTemplate(User user, string templateId)
        {
            if (IsValidUser(user))
            {
                Template template = TemplateSet.GetTemplate(int.Parse(templateId));

                while (ListSet.IsListExists(user, template.Name))
                {
                    template.Name = template.Name + "_copy";
                }

                List<TemplateItem> items = TemplateItemSet.GetTemplateItems(int.Parse(templateId));

                List list = new List()
                {
                    Name = template.Name,
                    Username = user.Username,
                    Color = template.Color,
                    LastModified = DateTime.Now.ToString(Constants.DATEFORMAT)
                };
                list.Id = ListSet.AddList(list);

                foreach (TemplateItem item in items)
                {
                    ListItem listItem = new ListItem()
                    {
                        Name = item.Name,
                        Quantity = item.Quantity,
                        Unit = item.Unit,
                        ListId = list.Id
                    };

                    ListItemSet.AddListItem(listItem);
                }

                return "List added successfully!";
            }
            return null;
        }
        #endregion

        #region ListItem
        public List<ListItem> GetListItems(User user, string listId)
        {
            if (IsValidUser(user))
            {
                return ListItemSet.GetListItems(long.Parse(listId));
            }
            return null;
        }

        public int GetListItemsCount(User user, string listId)
        {
            if (IsValidUser(user))
            {
                return ListItemSet.CountListItems(listId);
            }
            return -1;
        }

        public string AddListItem(User user, ListItem listItem)
        {
            if (IsValidUser(user))
            {
                return ListItemSet.AddListItem(listItem).ToString();
            }
            return "-1";
        }

        public int GetReminderCount(string username, string date)
        {
            return ListItemSet.GetReminderCount(username, date);
        }

        public bool UpdateListItem(User user, ListItem listItem, bool addToHistory)
        {
            if (IsValidUser(user))
            {
                if (addToHistory)
                {
                    ListItem item = ListItemSet.GetListItem(listItem.Id);
                    if (item != null)
                    {
                        ItemHistorySet.AddItemHistory(new ItemHistory()
                        {
                            Name = item.Name,
                            Quantity = item.Quantity,
                            Unit = item.Unit,
                            Date = DateTime.Now.ToShortDateString(),
                            Username = user.Username
                        });
                    }
                }

                return ListItemSet.UpdateListItem(listItem);
            }
            return false;
        }

        public bool DeleteListItem(User user, string itemId, string time)
        {
            if (IsValidUser(user))
            {
                return ListItemSet.DeleteListItem(int.Parse(itemId), time);
            }

            return false;
        }
        #endregion

    }
}
