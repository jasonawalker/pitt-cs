/*
 * "pi_driver, world!" minimal kernel module - /dev version
 *
 * Valerie Henson <val@nmt.edu>
 *
 */

#include <linux/fs.h>
#include <linux/init.h>
#include <linux/miscdevice.h>
#include <linux/module.h>
#include <asm/uaccess.h>
#include "pi.h"

/*
 * pi_driver_read is the function called when a process calls read() on
 * /dev/pi_driver.  It writes "pi_driver, world!" to the buffer passed in the
 * read() call.
 */

static ssize_t pi_driver_read(struct file * file, char * buf,
			  size_t count, loff_t *ppos)
{
	unsigned int size;
	char *buffer;
	long copy_ret;

	size = *ppos + count + 4;
	buffer = (char*)kmalloc(size, GFP_KERNEL);

	pi(buffer, size);

	//copy_ret is to handle the error given when the return value of copy_to_user is ignored
	copy_ret = copy_to_user(buf, buffer, count);

	kfree(buffer);
	return count;
}

/*
 * The only file operation we care about is read.
 */

static const struct file_operations pi_driver_fops = {
	.owner		= THIS_MODULE,
	.read		= pi_driver_read,
};

static struct miscdevice pi_driver_dev = {
	/*
	 * We don't care what minor number we end up with, so tell the
	 * kernel to just pick one.
	 */
	MISC_DYNAMIC_MINOR,
	/*
	 * Name ourselves /dev/pi_driver.
	 */
	"pi_driver",
	/*
	 * What functions to call when a program performs file
	 * operations on the device.
	 */
	&pi_driver_fops
};

static int __init
pi_driver_init(void)
{
	int ret;

	/*
	 * Create the "pi_driver" device in the /sys/class/misc directory.
	 * Udev will automatically create the /dev/pi_driver device using
	 * the default rules.
	 */
	ret = misc_register(&pi_driver_dev);
	if (ret)
		printk(KERN_ERR
		       "Unable to register \"pi_driver, world!\" misc device\n");

	return ret;
}

module_init(pi_driver_init);

static void __exit
pi_driver_exit(void)
{
	misc_deregister(&pi_driver_dev);
}

module_exit(pi_driver_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Jason Walker <jaw280@pitt.edu>");
MODULE_DESCRIPTION("pi module");
MODULE_VERSION("dev");
