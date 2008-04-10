package gov.nih.nci.cananolab.util;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.service.common.LookupService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utilility class to handle domain class manipulations
 * 
 * @author pansu
 * 
 */
public class ClassUtils {
	/**
	 * Get all caNanoLab domain classes
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Collection<Class> getDomainClasses() throws Exception {
		Collection<Class> list = new ArrayList<Class>();
		JarFile file = null;
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				"application-config.xml");
		
		File webinfDirect= (new File(url.getPath())).getParentFile().getParentFile();
		
		String fullJarFilePath = webinfDirect + File.separator + "lib"
				+ File.separatorChar + CaNanoLabConstants.SDK_BEAN_JAR;
		file = new JarFile(fullJarFilePath);

		if (file == null)
			throw new Exception("Could not locate the bean jar");
		Enumeration e = file.entries();
		while (e.hasMoreElements()) {
			JarEntry o = (JarEntry) e.nextElement();
			if (!o.isDirectory()) {
				String name = o.getName();
				if (name.endsWith(".class")) {
					String klassName = name.replace('/', '.').substring(0,
							name.lastIndexOf('.'));
					list.add(Class.forName(klassName));
				}
			}
		}
		return list;
	}

	/**
	 * Get child classes of a parent class in caNanoLab
	 * 
	 * @param parentClassName
	 * @return
	 * @throws Exception
	 */
	public static List<Class> getChildClasses(String parentClassName)
			throws Exception {
		Collection<Class> classes = getDomainClasses();
		List<Class> childClasses = new ArrayList<Class>();
		for (Class c : classes) {
			if (c.getSuperclass().getName().equals(parentClassName)) {
				childClasses.add(c);
			}
		}
		return childClasses;
	}

	/**
	 * Get child class names of a parent class in caNanoLab
	 * 
	 * @param parentClassName
	 * @return
	 * @throws Exception
	 */
	public static List<String> getChildClassNames(String parentClassName)
			throws Exception {
		List<Class> childClasses = getChildClasses(parentClassName);
		List<String> childClassNames = new ArrayList<String>();
		for (Class c : childClasses) {
			childClassNames.add(c.getCanonicalName());
		}
		return childClassNames;
	}

	/**
	 * get the short class name without fully qualified path
	 * @param className
	 * @return
	 */
	public static String getShortClassName(String className) {
		String[] strs = className.split("\\.");
		return strs[strs.length - 1];
	}

	/**
	 * check if a class has children classes
	 * @param parent class name
	 * @return
	 */
	public static boolean hasChildrenClasses(String parentClassName) throws Exception {
		boolean hasChildernFlag = false;
		if(parentClassName == null) {
			return hasChildernFlag;
		}
		List<String> subclassList = ClassUtils
			.getChildClassNames(parentClassName);
		if(subclassList == null || subclassList.size() == 0)
			hasChildernFlag = false;
		else
			hasChildernFlag = true;
		
		return hasChildernFlag;
	}
	
	public static void main(String[] args) {
		try {
			List<String> names = ClassUtils
					.getChildClassNames("gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			for (String name : names) {
				System.out.println(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
