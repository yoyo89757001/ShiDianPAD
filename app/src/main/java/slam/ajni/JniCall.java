package slam.ajni;

public class JniCall {
	public static boolean mLoadLibraryOk = false;
	static {
		try {

			 System.load("/data/so/libAJni-Mid.so");
			// System.load("libAJni_Mid.so");
//			System.loadLibrary("AJni_Mid");
			mLoadLibraryOk = true;

		} catch (Exception e) {
			e.printStackTrace();
			mLoadLibraryOk = false;
		}
	}

	public Psam_information_c testInfo;
	public Psam_information_cls testInform;
	
	private JniCall() {
		testInfo = new Psam_information_c();
		testInform = new Psam_information_cls();
	}

	private static JniCall mJniCall = new JniCall();

	public static JniCall getInstance() {
		return mJniCall;
	}

	public native int Mini_test(byte[] test);

	// 1握手
	public native int Mini_Init(int osType);

	// 2释放
	public native int Mini_release();

	// 3LOG
	public native int Mini_log_switch(byte szLogSw);

	// 4超时时间
	public native int Mini_wait_timeout_set(int iTimeout);

	// 5获取bootloader(0x01)、驱0x02)、应0x03)、硬0x04)、协?0x05)版本
	public native int Mini_version_get(byte szVerType, byte[] szVersion);

	// 6获取厂家编码
	public native int Mini_manufacturers_get(byte[] szMcode);

	// 7电源
	public native int Mini_battery_detect(byte[] szBatType, short[] szBatVoltage);

	// 8电源状?更新
	public native int Mini_battery_update(byte szBatType, short szBatVoltage);

	// 9低功耗进?
	public native int Mini_lowpower_init();

	// 10低功耗??
	public native int Mini_lowpower_deinit();

	// 11系统属?设置 （预留接?可扩展）
	public native int Mini_system_property_set(byte system_type, byte print_type);

	// 1LED
	public native int Mini_led_control(byte szLedType, byte szLedSw);

	// PSAM?
	// 1PSAM上电
	public native int Mini_psam_pwrup(byte szSlotIdx, byte szVolType,
			short[] nAtrLen, byte[] szAtr);

	// 2PSAM下电
	public native int Mini_psam_pwrdown(byte szSlotIdx);

	// 3获取PSAM卡所在卡
	public native byte Mini_psam_detect();

	// 4切换PSAM卡所在卡
	public native byte Mini_psam_slotidx_sel(byte szSlotIdx);

	// 5读PSAM卡id
	public native byte Mini_psam_id_get(byte szCardIdx, byte[] szMasterKey,
			byte[] szCardID, byte[] szDate);

	/*
	 * 【函数? byte Mini_psam_information_get(Psam_information info, byte
	 * szCardIdx, byte[] szMasterKey, byte [] szAppKey); 【功能?获取PSAM卡信?【参数?
	 * info，出参， PSAM卡所有信?szCardIdx：入参，卡槽索引 0x01 - 卡槽1 0x02 - 卡槽2
	 * szMasterKey：入参，卡片主控密钥?8字节 szAppKey：入参，应用密钥?8字节 【返回?获取结果?1 - 获取成功 -1 -
	 * 获取失败 -128 - 外设动?库加载失?
	 */
	public class Psam_information_cls {
		public byte type; // 连接方式标志?
		public byte[] FSK_tel; // FSK拨号号码21byte
		public byte[] FSK_tel_bak; // FSK备用拨号号码21byte
		public byte[] apn; // GPRS连接APN参数31byte
		public byte[] user; // GPRS连接用户名（ 31byte
		public byte[] password; // GPRS连接密码?31byte
		public byte[] SMSC; // 短信中心号（ 21byte
		public byte[] SMSC_bak; // 备用短信中心号（ 21byte
		public byte[] master_ip; // 主服务器连接IP地址?4byte
		public short master_sendport; // 主服务器连接发?端口
		public short master_recvport; // 主服务器连接接收端口
		public byte[] master_ip_bak; // 主服务器连接备用IP地址4byte
		public short master_sendport_bak; // 主服务器连接备用发端口
		public short master_recvport_bak; // 主服务器连接备用接收端口
		public byte[] upgrade_ip; // 升级服务器连接`P地址?4byte
		public short upgrade_sendport; // 升级服务器连接发送端
		public short upgrade_recvport; // 升级服务器连接接收端
		public byte[] upgrade_ipbak; // 备用升级服务器连接`P地址?4byte
		public short upgrade_sendport_bak; // 备用升级服务器连接发送端
		public short upgrade_recvport_bak; // 备用升级服务器连接接收端
		public byte[] binding_no; // 绑定业务号码21byte?
		public byte[] service_id; // 服务网点ID?16byte
		public byte[] service_addr; // 服务网点地址，以/0结束256byte
		public byte[] agent_name; // 代理商名，0结束?31byte
		public byte[] service_name; // 服务网点名称，以/0结束51byte
		public byte[] service_tel; // 服务网点联系电话，以/0结束21byte
		public byte[] user_tel; // 安裑点联系电话（ 21byte
		public byte[] user_addr; // 安裑点地 256byte
		public byte[] psam_id; // PSAM卡卡?以s0结束17byte
		public byte[] date; // 有效日期,保存格式为：（ 8byte
		// 2010-06-28~~2012-06-27表示
		// 0x20 0x10 0x06 0x28 0x20 0x12 0x06 0x27
		public byte[] modify_date; // 修改日期7byte
		public byte[] modify_name; // 修改人员?10byte
		public byte[] modify_txt; // 修改内容?20byte
		public byte[] version; // 软件版本?2byte
	};

	public class Psam_information_c {
		public byte[] service_id; // 服务网点ID?16byte
		public byte[] service_addr; // 服务网点地址，以/0结束256byte
		public byte[] agent_name; // 代理商名，?0结束?31byte
		public byte[] service_name; // 服务网点名称，以/0结束?51byte?
		public byte[] service_tel; // 服务网点联系电话，以/0结束""?21byte?
		public byte[] user_tel; // 安裑点联系电话（ 21byte?
		public byte[] user_addr; // 安裑点地""?"""" 256byte?
	}

	// 6读PSAM卡信息，返回结构""?
	public native byte Mini_psam_information_get(Psam_information_cls info,
			byte szCardIdx, byte[] szMasterKey, byte[] szAppKey);

	// 7校验密码
	public native byte Mini_psam_poweron_password_vef(byte[] szPin,
			byte szPinLen, byte[] szRemainTimes, byte szCardIdx,
			byte[] szMasterKey);

	// 8修改密码
	public native byte Mini_psam_poweron_password_modify(byte[] szOldPin,
			byte szOldPinLen, byte[] szNewPin, byte szNewPinLen,
			byte[] szRemainTimes, byte szCardIdx, byte[] szMasterKey);

	// 9PSAM卡加""?
	public native byte Mini_psam_des_encrypt(byte[] szDataIn, byte szDataInLen,
			byte szCardIdx, byte[] szMasterKey, byte szDesType, byte[] szDataOut);

	// 10PSAM卡APDU
	public native byte Mini_psam_apdu(byte[] szApdu, char nApduLen,
			byte szCardIdx, byte[] szAck, char[] nAckLen);

	// 11验证PSAM卡开机密""?
	public native int Mini_psam_logon_pswd_verify(byte[] szPin, byte szPinLen,
			byte szCardIdx, byte[] szMasterKey, byte[] szRemainTimes);

	// 12搜寻PSAM""?
	public native int Mini_psam_search(byte[] szMasterKey, byte[] szCardStatus,
			byte[] szModifyDate1, byte[] szCardNum1, byte[] szValidDate1,
			byte[] szModifyDate2, byte[] szCardNum2, byte[] szValidDate2);

	// 13获取PSAM卡用户信""?
	public native int Mini_psam_userinfo_get(byte[] szPin, byte szPinLen,
			byte szCardIdx, byte[] szMasterKey, byte[] szRemainTimes,
			Psam_information_c info);

	// 14读取PSAM卡修改历史记""?
	public native int Mini_psam_history_get(byte szCardNo, byte[] szMasterKey,
			byte[] szCardID, byte[] szDate, byte[] szModifyDate,
			byte[] szModifyPerson, byte[] szModifyContent, byte[] szSoftVersion);

	// 联""?白卡
	// 1获取ICCID
	public native int Mini_sim_iccid_get(byte[] szIccid, byte[] szIccidLen);

	// 2""?""""白卡
	public native int Mini_sim_blank_check(byte szSimType, byte[] szCheckRes,
			byte[] szImsi2G, byte[] szImsiLen2G, byte[] szImsi3G,
			byte[] szImsiLen3G);

	// 3写IMSI号码
	public native int Mini_sim_imsi_write(byte[] szImsi1, byte[] szImsi2);

	// 4白卡写短信中""?
	public native int Mini_sim_smsc_write(byte[] szSmsc, byte szSlotIdx);

	// 5白卡写预置信""?
	public native int Mini_sim_info_write(byte szMsgType, byte szRecId,
			byte szMsgLen, byte[] szMsg, byte szSlotIdx);

	// 6白卡读预置信""?
	public native int Mini_sim_info_read(byte szMsgType, byte szRecID,
			byte[] szMsgLen, byte[] szMsg, byte szSlotIdx);

	// 1""?""""打印""?
	public native int Mini_printer_start();

	// 2关闭
	public native  int Mini_printer_stop();

	// 3查询状""?
	public native int Mini_printer_status_get();

	// 4参数设置
	public native int Mini_printer_parameter_set(byte[] szParams);

	// 5打印文本
	public native int Mini_printer_font_print(byte szPrintType, byte[] szPrintStr);

	// 6打印图片
	public native int Mini_printer_bmp_print(short nWidth, short nHeight, byte[]
			szBmpData, byte szLocation, byte szTopSpace, byte szBottomSpace);

	// 7是否有纸
	public native int Mini_printer_IsPaperExist();

	// 8打印机温""?
	public native  int Mini_printer_TempDet(int[] nTemp);

	// 9设置打印机浓""?
	public native int Mini_printer_density_set(byte szDensity);

	// 1初妾""?
	public native int Mini_modem_init();

	// 2模块复位
	public native int Mini_modem_reset();

	// 3接收数据
	public native int Mini_modem_rec_data(byte[] rec_data, int[] rec_len,
			byte time_out);

	// 4卸载
	public native int Mini_modem_deinit();

	// PS2""?
	// 1PS2口初始化
	public native int Mini_ps2_init(int nBaudrate, int nDatabits,
			int nStopbits, int nParity);

	// 2PS2异口初妾""?
	public native int Mini_ps2_port_init(String deviceid, int nBaudrate,
			int nDatabits, int nStopbits, int nParity);

	// 3PS2口发送数""?
	public native int Mini_ps2_write(byte[] szSendData, short nSendLen);

	// 4PS2口读取数""?
	public native int Mini_ps2_read(byte[] szRecv);

	// 5PS2口扩展读取数""?
	public native int Mini_ps2_read(byte[] szRecv, short[] szRecvlen,
			int timeout);

	// 6PS2口读取数据""?""?
	public native int Mini_ps2_read_exit();

	// 7PS2口卸""?
	public native int Mini_ps2_deinit();

	// 1dataflah""?""""
	public native int Mini_dataflash_detect();

	// 2进入透传
	public native int Mini_idcard_enter_transparent();

	// 3""?""""透传
	public native int Mini_idcard_exit_transparent();

	// 4查找二代""?
	public native int Mini_rfid_card_is_find(int nTimeout);

	// 5搜寻二代证设""?
	public native int Mini_idcard_device_detect();

	// 6二代证识""?
	public native int Mini_idcard_read(short nTimeout, byte[] cardinfo,
			short[] len);

	// 1刷卡
	public native int Mini_magnetic_swipe(byte szTimeout, byte[] szTrackLen,
			byte[] szTrack1, byte[] szTrack2, byte[] szTrack3);

	// 升级
	public native int Mini_m3_updata(byte[] path_name, byte[] file_name,
			byte file_type, int updata_sum);

	// 摄像头led""?""""
	public native int Mini_camera_ledsw(byte on1_off0);

	public int setCameraLedSwitch(byte on1_off0) {
		return Mini_camera_ledsw(on1_off0);
	}
}
