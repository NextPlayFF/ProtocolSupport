package protocolsupport.injector.network;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.NetworkManager;
import net.minecraft.server.v1_11_R1.ServerConnection;
import protocolsupport.utils.ReflectionUtils;
import protocolsupport.utils.nms.MinecraftServerWrapper;

public class NettyInjector {

	@SuppressWarnings("unchecked")
	public static void inject() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		ServerConnection serverConnection = MinecraftServerWrapper.getServer().an();
		List<NetworkManager> nmList = null;
		try {
			nmList = (List<NetworkManager>) ReflectionUtils.setAccessible(ServerConnection.class.getDeclaredField("pending")).get(serverConnection);
		} catch (NoSuchFieldException e) {
			nmList = (List<NetworkManager>) ReflectionUtils.setAccessible(ServerConnection.class.getDeclaredField("h")).get(serverConnection);
		}
		Field connectionsListField = ReflectionUtils.setAccessible(ServerConnection.class.getDeclaredField("g"));
		ChannelInjectList connectionsList = new ChannelInjectList(nmList, (List<ChannelFuture>) connectionsListField.get(serverConnection));
		connectionsListField.set(serverConnection, connectionsList);
		connectionsList.injectExisting();
	}

	public static class ChannelInjectList implements List<ChannelFuture> {

		private final List<NetworkManager> networkManagersList;
		private final List<ChannelFuture> originalList;
		public ChannelInjectList(List<NetworkManager> networkManagerList, List<ChannelFuture> originalList) {
			this.originalList = originalList;
			this.networkManagersList = networkManagerList;
		}

		public void injectExisting() {
			for (ChannelFuture future : originalList) {
				inject(future);
			}
		}

		@Override
		public boolean add(ChannelFuture e) {
			boolean result = originalList.add(e);
			inject(e);
			return result;
		}

		@Override
		public void add(int index, ChannelFuture element) {
			originalList.add(index, element);
			inject(element);
		}

		@Override
		public ChannelFuture set(int index, ChannelFuture element) {
			ChannelFuture result = originalList.set(index, element);
			inject(element);
			return result;
		}

		@Override
		public boolean addAll(Collection<? extends ChannelFuture> c) {
			boolean result = originalList.addAll(c);
			for (ChannelFuture future : c) {
				inject(future);
			}
			return result;
		}

		@Override
		public boolean addAll(int index, Collection<? extends ChannelFuture> c) {
			boolean result = originalList.addAll(index, c);
			for (ChannelFuture future : c) {
				inject(future);
			}
			return result;
		}

		protected void inject(ChannelFuture future) {
			Channel channel = future.channel();
			channel.pipeline().addFirst(new NettyServerChannelHandler());
			synchronized (networkManagersList) {
				for (NetworkManager nm : networkManagersList) {
					if (nm.channel.localAddress().equals(channel.localAddress())) {
						nm.close(new ChatComponentText("ProtocolSupport channel reset"));
					}
				}
			}
		}

		@Override
		public int size() {
			return originalList.size();
		}

		@Override
		public boolean isEmpty() {
			return originalList.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return originalList.contains(o);
		}

		@Override
		public Iterator<ChannelFuture> iterator() {
			return originalList.iterator();
		}

		@Override
		public Object[] toArray() {
			return originalList.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return originalList.toArray(a);
		}

		@Override
		public boolean remove(Object o) {
			return originalList.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return originalList.containsAll(c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return originalList.removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return originalList.retainAll(c);
		}

		@Override
		public void clear() {
			originalList.clear();
		}

		@Override
		public ChannelFuture get(int index) {
			return originalList.get(index);
		}

		@Override
		public ChannelFuture remove(int index) {
			return originalList.remove(index);
		}

		@Override
		public int indexOf(Object o) {
			return originalList.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o) {
			return originalList.lastIndexOf(o);
		}

		@Override
		public ListIterator<ChannelFuture> listIterator() {
			return originalList.listIterator();
		}

		@Override
		public ListIterator<ChannelFuture> listIterator(int index) {
			return originalList.listIterator(index);
		}

		@Override
		public List<ChannelFuture> subList(int fromIndex, int toIndex) {
			return originalList.subList(fromIndex, toIndex);
		}

	}

}
