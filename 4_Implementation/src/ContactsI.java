import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Locale;

import java.util.Arrays;

public class ContactsI
{

	public static void WriteRecord(String mfile, String name, String surname, String contact) 
    {
        String[] lines = getLines(mfile);
        
        String[] names = new String[lines.length];
        String[] surnames = new String[lines.length];
        String[] numbers = new String[lines.length];


        for (int i = 0; i < lines.length; i++)
        {
            String[] fields = interpretLine(lines[i]);
            names[i] = fields[0];
            surnames[i] = fields[1];
            numbers[i] = fields[2];
        }

        deleteFile(mfile);

        int i = 0;
        while ((i < names.length) && (names[i].compareTo(name) < 0))
        {
            i++;
        }
        while ((i < names.length) && (names[i].equals(name)) && (surnames[i].compareTo(surname) < 0))
        {
            i++;
        }

        String[] f = new String[lines.length+1];
        String[] s = new String[lines.length+1];
        String[] n = new String[lines.length+1];
        
        int cnt = 0;
        while (cnt < i)
        {
            f[cnt] = names[cnt];
            s[cnt] = surnames[cnt];
            n[cnt] = numbers[cnt];
            cnt++;
        } 
        f[cnt] = name;
        s[cnt] = surname;
        n[cnt] = contact;
        cnt++;
        while (cnt < lines.length+1)
        {
            f[cnt] = names[cnt-1];
            s[cnt] = surnames[cnt-1];
            n[cnt] = numbers[cnt-1];
            cnt++;
        }

        //System.out.println(Arrays.toString(f));

		try (PrintWriter w = new PrintWriter(new FileWriter(mfile, true))) 
        {
            for (i = 0; i < f.length; i++)
            {
			    w.println(f[i] + "," + s[i] + "," + n[i]);
            }
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
	}

	public static void newContact(String mfile) 
    {
		Scanner s = new Scanner(System.in);
		System.out.println("Add a conatct?  <y> for yes.");
		if (!s.nextLine().equals("y")) 
        {
			return;
		}
		while (true) 
        {
			System.out.print("Conatct first name: ");
			String a = s.nextLine();
            System.out.print("Conatct surname: ");
			String b = s.nextLine();
			System.out.print("Phone number: ");
			String c = s.nextLine();
			WriteRecord(mfile, a, b, c);
			//s.nextLine(); // grab the newline
			System.out.println("Add another conatct?  Type <y> to add another conatct, anything else to quit.");
			if (!s.nextLine().equals("y")) 
            {
				break;
			}
		}
	}
	
    public static String getTarget (String t)
    {
        Scanner in = new Scanner(System.in);
        if (t.equals("1"))
        {
            System.out.printf("Please enter a first name or part thereof: ");  
            return in.nextLine(); 
        }
        if (t.equals("2"))
         {
            System.out.printf("Please enter a surname or part thereof: ");   
            return in.nextLine(); 
        }
        if (t.equals("3"))   
        {
            System.out.printf("Please enter a number or part thereof: ");   
            return in.nextLine(); 
        }
        return "";
    }


	public static boolean matchLine(String data, String target, String field, int n) 
    {
		Scanner s = new Scanner(data).useDelimiter(",");
		String name = s.next();
	    String surname = s.next();
		String number = s.next();
        if (field.equals("1"))
        {
            if (name.toLowerCase().indexOf(target.toLowerCase()) >=0)
            {
		        System.out.println(n + ". " + name + " " + surname + " : " + number);
                return true;
            }
        }
        if (field.equals("2"))
        {
            if (surname.toLowerCase().indexOf(target.toLowerCase()) >=0)
            {
		        System.out.println(n + ". " + name + " " + surname + " : " + number);
                return true;
            }
        }
        if (field.equals("3"))
        {
            if (number.toLowerCase().indexOf(target.toLowerCase()) >=0)
            {
		        System.out.println(n + ". " + name + " " + surname + " : " + number);
                return true;
            }
        }
        return false;
	}
	
	public static void findTargets(String mfile, String t, String f) 
    {
		Path p = Paths.get(mfile);
		try 
        {
			String[] lines = Files.readAllLines(p).toArray(new String[0]);
            int cnt = 0;
			for (String line : lines) 
            {
				//System.out.println("LINE: " + line);
				if (matchLine(line, t, f, cnt+1))
                {
                    cnt++;
                    if (cnt % 10 == 0)
                    {
                        Scanner s = new Scanner(System.in);
                        System.out.println ("\n<s> to stop.  Any <Enter> to continue");
			            if (s.nextLine().toLowerCase().equals("s")) 
                        {
				            break;
                        }
                    }
                }
			}
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
	}

	public static void findContact(String mfile) 
    {
        String ans = "";
        Scanner in = new Scanner(System.in);
        String target = "";
        while (true)
        {
            System.out.printf("\n\nDo you wish to:\n");
            System.out.printf("[1] Search by name\n");
            System.out.printf("[2] Search by surname\n");
            System.out.printf("[3] Search by number\n");
            System.out.printf("[q] Return to previous menu\n");
            ans = in.next().toLowerCase();
            if (!ans.equals("q"))
            {
                target = getTarget(ans);
                findTargets(mfile, target, ans);
            }
            if (ans.toLowerCase().equals("q"))
            {
                break;
            }
        }
    }

	public static String[] delMatch(String data, String target, String field, int n) 
    {
		Scanner s = new Scanner(data).useDelimiter(",");
		String name = s.next();
	    String surname = s.next();
		String number = s.next();
        if (field.equals("1"))
        {
            if (name.toLowerCase().indexOf(target.toLowerCase()) >=0)
            {
		        System.out.println(n + ". " + name + " " + surname + " : " + number);
                return new String[] {name, surname, number};
            }
        }
        if (field.equals("2"))
        {
            if (surname.toLowerCase().indexOf(target.toLowerCase()) >=0)
            {
		        System.out.println(n + ". " + name + " " + surname + " : " + number);
                return new String[] {name, surname, number};
            }
        }
        if (field.equals("3"))
        {
            if (number.toLowerCase().indexOf(target.toLowerCase()) >=0)
            {
		        System.out.println(n + ". " + name + " " + surname + " : " + number);
                return new String[] {name, surname, number};
            }
        }
        return new String[] {};
	}
 
    public static String[] selectOne(String[] lines, String t, String f)
    {
            String[] matches = {};
            int cnt = 0;
			for (String line : lines) 
            {
				//System.out.println("LINE: " + line);
                String[] dm = delMatch(line, t, f, cnt+1);
				if (dm.length > 1)
                {
                    String[] nm = new String[matches.length+3];
                    for (int i = 0; i < matches.length; i++)
                    {
                        nm[i] = matches[i];
                    }
                    nm[matches.length] = dm[0]; 
                    nm[matches.length+1] = dm[1];
                    nm[matches.length+2] = dm[2];
                    matches = nm;
                    cnt++;
                    if (cnt % 10 == 0)
                    {
                        Scanner s = new Scanner(System.in);
                        System.out.println ("\n<s> to stop.  Any <Enter> to continue");
			            if (s.nextLine().toLowerCase().equals("s")) 
                        {
				            break;
                        }
                    }
                }
			}
            //System.out.println(Arrays.toString(matches));
            return matches;
    }   

	public static String[] targetToDelete (String mfile, String t, String f) 
    {
		Path p = Paths.get(mfile);
        String[] results = {};
		try 
        {
			String[] lines = Files.readAllLines(p).toArray(new String[0]);
            results = selectOne(lines, t, f);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        return results;
	}

	public static String[] getLines (String mfile) 
    {
		Path p = Paths.get(mfile);
        String[] lines = {};
		try 
        {
			lines = Files.readAllLines(p).toArray(new String[0]);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        return lines;
	}

	public static void deleteFile (String mfile) 
    {
		Path p = Paths.get(mfile);
		try 
        {
			Files.deleteIfExists(p);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
	}

	public static void writeLine(String mfile, String data, String dname, String dsname, String dnum) 
    {
		Scanner s = new Scanner(data).useDelimiter(",");
		String name = s.next();
	    String surname = s.next();
		String number = s.next();
        //System.out.println(dname + " " + dsname + " " + dnum);
        if (!name.equals(dname) || !surname.equals(dsname) || !number.equals(dnum))
        {
		    WriteRecord(mfile, name, surname, number);
        }
	}

	public static void deleteContact(String mfile) 
    {
        String ans = "";
        Scanner in = new Scanner(System.in);
        String target = "";
        String[] results = {}; 
        int selected = -1;

        System.out.printf("\n\nDo you wish to:\n");
        System.out.printf("[1] Delete by name\n");
        System.out.printf("[2] Delete by surname\n");
        System.out.printf("[q] Return to previous menu\n\n");
        ans = in.next().toLowerCase();
        if (ans.equals("1") || ans.equals("2"))
        {
            target = getTarget(ans);
            results = targetToDelete(mfile, target, ans);
        }
        if (results.length > 1)
        {
            System.out.printf("\n0. Return to previous menu\n\n");
            while (true)
            {
                System.out.print("Which one do you wish to delete? ");
                selected = in.nextInt();
                if (selected >= 0 && selected <= results.length/3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid selection");
                }
            }
        }
        if (selected != 0)
        {
            String[] lines = getLines(mfile);
            deleteFile(mfile);
            for(String line:lines)
            {
                writeLine(mfile, line, results[(selected-1)*3], results[(selected-1)*3 + 1],results[(selected-1)*3+2]);
            }
        }
    }

	public static String[] interpretLine(String data) 
    {
		Scanner s = new Scanner(data).useDelimiter(",");
		String name = s.next();
	    String surname = s.next();
		String number = s.next();
		return new String[] {name, surname, number};
	}
	
    public static String[] swap (String[] sarr, int x, int y)
    {
        String s = sarr[x];
        sarr[x] = sarr[y];
        sarr[y] = s;
        return sarr;
    }
    public static void sortContacts (String[] lines, String[] f, String[] s, String[] n)
    {
        for (int x = 0; x < f.length; x++)
        {
            for (int y = 1; y < f.length; y++)
            {
                if (f[y-1].compareTo(f[y]) > 0)
                {
                    f = swap(f, y, y-1);
                    s = swap(s, y, y-1);
                    n = swap(n, y, y-1); 
                } 
            }
        }
      for (int start = 0; start < f.length; start++)
        {
            int p = start;
            while (p+1 < f.length && f[p].equals(f[p+1]))
            {
                p++;
            }
            if (p > start)
            {
                for (int i = start; i <= p; i++)
                {
                    //System.out.println(names[i]);
                    //surnames[i] = "XXXXXXXXXX";
                    for (int j = start+1; j <= p; j++)
                    {
                        if (s[j-1].compareTo(s[j]) > 0)
                        {
                            f = swap(f, j, j-1);
                            s = swap(s, j, j-1);
                            n = swap(n, j, j-1);
                        }   
                    }
                }
                    
            }
            start = p;
        }
    }

	public static void showContacts(String mfile) 
    {
        Scanner in = new Scanner(System.in);
        System.out.printf("\n\nDo you wish to:\n");
        System.out.printf("[1] Sort by name\n");
        System.out.printf("[2] Sort by surname\n");
        System.out.printf("[q] Return to previous menu\n\n");
        String ans = in.next().toLowerCase();
        if (ans.equals("1") || ans.equals("2"))
        {    
    		Path p = Paths.get(mfile);
    		try 
            {
    			String[] lines = Files.readAllLines(p).toArray(new String[0]);
                String[] names = new String[lines.length];
                String[] surnames = new String[lines.length];
                String[] numbers = new String[lines.length];


                for (int i = 0; i < lines.length; i++)
                {
                    String[] fields = interpretLine(lines[i]);
                    names[i] = fields[0];
                    surnames[i] = fields[1];
                    numbers[i] = fields[2];
                }
                if (ans.equals("2"))
                {
                    sortContacts(lines, surnames, names, numbers);
                }
                else
                {
                    sortContacts(lines, names, surnames, numbers);
                }
       			for (int cnt = 0; cnt < lines.length; cnt++) 
                {
				//System.out.println("LINE: " + line);

                    System.out.println (cnt+1 + "." + names[cnt] + " " + surnames[cnt] + " : " + numbers[cnt]);


                    if ((cnt+1) % 10 == 0)  
                    {
                        Scanner s = new Scanner(System.in);
                        System.out.println ("\n<s> to stop.  Any <Enter> to continue");
    			        if (s.nextLine().toLowerCase().equals("s")) 
                        {
			    	        break;
                        }
                    }
			    }
		    } 
            catch (IOException e) 
            {
			    e.printStackTrace();
		    }
           
	    }
    }
	

    public static void main(String[] args)
    {
        final String my_file = "contacts_sorted.csv";
        String ans = "";
        Scanner in = new Scanner(System.in);
        while (true)
        {
            System.out.printf("\n\nWould you like to:\n");
            System.out.printf("[1] Enter a new contact\n");
            System.out.printf("[2] Show all contacts\n");
            System.out.printf("[3] Find a contact\n");
            System.out.printf("[4] Delete a contact\n");
            System.out.printf("[q] Quit\n");
            ans = in.next().toLowerCase();
            if (ans.equals("1"))
            {
                newContact(my_file);
            }
            if (ans.equals("2"))
            {
                showContacts(my_file);
            }
            if (ans.equals("3"))
            {
                findContact(my_file);
            }
            if (ans.equals("4"))
            {
                deleteContact(my_file);
            }
            if (ans.toLowerCase().equals("q"))
            {
                break;
            }
        }
    }
}
