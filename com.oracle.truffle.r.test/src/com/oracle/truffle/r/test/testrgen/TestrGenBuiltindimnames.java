/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.testrgen;

import org.junit.*;

import com.oracle.truffle.r.test.*;

// Checkstyle: stop line length check
public class TestrGenBuiltindimnames extends TestBase {

    @Test
    public void testdimnames1() {
        assertEval("argv <- list(structure(c(4L, 3L, 2L, 1L, 2L), .Label = c(\'0.6\', \'0.8\', \'Area Examined\', \'C2\'), class = \'factor\'));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames2() {
        assertEval("argv <- list(structure(c(0, 0, 0, 0, 0, 56.989995924654, 56.989995924654, 94.3649041101607, 94.3649041101607, 94.3649041101607, 94.3649041101607, 94.3649041101607, 94.3649041101607, 109.608811230383, 109.608811230383, 109.608811230383, 107.478028232287, 107.478028232287, 107.478028232287, 107.478028232287, 94.6057793667664, 94.6057793667664, 94.6057793667664, 94.6057793667664, 94.6057793667664, 94.6057793667664, 76.6771074226725, 76.6771074226725, 76.6771074226725, 76.6771074226725, 76.6771074226725, 76.6771074226725, 76.6771074226725, 76.6771074226725, 76.6771074226725, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 57.5975949121373, 39.6403646307366, 39.6403646307366, 39.6403646307366, 39.6403646307366, 39.6403646307366, 10.7055301785859, 0, 1.00000000551046, 1.00000000551046, 1.00000000551046, 1.00000000551046, 1.00000000551046, 0.914597467778369, 0.914597467778369, 0.764820801027804, 0.764820801027804, 0.764820801027804, 0.764820801027804, 0.764820801027804, 0.764820801027804, 0.599195286063472, 0.599195286063472, 0.599195286063472, 0.446659102876937, 0.446659102876937, 0.446659102876937, 0.446659102876937, 0.319471715663991, 0.319471715663991, 0.319471715663991, 0.319471715663991, 0.319471715663991, 0.319471715663991, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.21965732107982, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.144322069921372, 0.0889140940358009, 0.0889140940358009, 0.0889140940358009, 0.0889140940358009, 0.0889140940358009, 0.0202635232425103, 2.60032456603692e-08, 0, 0, 0, 0, 0, 0.165626203544259, 0.165626203544259, 0.341691261149167, 0.341691261149167, 0.341691261149167, 0.341691261149167, 0.341691261149167, 0.341691261149167, 0.503396799290371, 0.503396799290371, 0.503396799290371, 0.638987326722699, 0.638987326722699, 0.638987326722699, 0.638987326722699, 0.746106779008021, 0.746106779008021, 0.746106779008021, 0.746106779008021, 0.746106779008021, 0.746106779008021, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.827421615259225, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.887496120452751, 0.931061257482989, 0.931061257482989, 0.931061257482989, 0.931061257482989, 0.931061257482989, 0.984387422945875, 0.999999996451695), .Dim = c(52L, 3L)));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames3() {
        assertEval("argv <- list(structure(c(FALSE, FALSE, FALSE), .Dim = c(3L, 1L)));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames4() {
        assertEval("argv <- list(structure(c(NA, 1, 2, 5.65604443125997, 8.44399377410362, 5.49523049516867, 71.3540021461976, 72.1419514890413, 75.1931882101063), .Dim = c(3L, 3L), .Dimnames = list(c(\'<none>\', \'- M.user:Temp\', \'+ Soft\'), c(\'Df\', \'Deviance\', \'AIC\'))));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames5() {
        assertEval("argv <- list(structure(c(0.0495149735282523, -0.108383943640066, 0.077846206317523, -0.0237949779538032, 0.00481774174809338, -0.108383943640066, 0.280303242453237, -0.276080245636638, 0.130604235856321, -0.0264432890328551, 0.077846206317523, -0.276080245636638, 0.443183251704797, -0.364557026828347, 0.119607814442664, -0.0237949779538032, 0.130604235856321, -0.364557026828347, 0.44886505191838, -0.191117282992552, 0.00481774174809338, -0.0264432890328551, 0.119607814442664, -0.191117282992552, 0.0931350158346494), .Dim = c(5L, 5L)));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames6() {
        assertEval("argv <- list(structure(c(3+2i, 3+2i, NA, 3+2i, 3+2i, 3+2i, 3+2i, 3+2i, 4-5i, 3-5i, NA, NA, 2-5i, 3-5i, 4-5i, 5-5i), .Dim = c(8L, 2L), .Dimnames = list(NULL, c(\'x1\', \'x2\'))));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames7() {
        assertEval("argv <- list(structure(c(13.0879058831551, -0.0481782079188499, 0.0648397936975344, -0.0703016880559154, -0.433062454113996, -0.000149473169823967, -16.3229386445345, -0.0481782079188499, 1.42936577525521, 0.00346026862477374, 0.000168722552167122, -0.00850959684180395, -9.2926002743558e-06, -1.44812039916227, 0.0648397936975344, 0.00346026862477374, 0.0649221455479854, 1.50206888047831e-06, 0.0303152177308945, -5.59890220792902e-06, -0.238079760031664, -0.0703016880559154, 0.000168722552167122, 1.50206888047831e-06, 0.00876007504795771, 0.000744776618395879, -6.15610217329725e-06, -0.0811419414051802, -0.433062454113996, -0.00850959684180395, 0.0303152177308945, 0.000744776618395879, 10.728754385628, -6.46786616103191e-05, -11.116657381748, -0.000149473169823967, -9.2926002743558e-06, -5.59890220792902e-06, -6.15610217329725e-06, -6.46786616103191e-05, 0.00193527894824396, -0.000812297378584339, -16.3229386445345, -1.44812039916227, -0.238079760031664, -0.0811419414051802, -11.116657381748, -0.000812297378584339, 249.99918229946), .Dim = c(7L, 7L)));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames8() {
        assertEval("argv <- list(structure(list(visible = c(TRUE, TRUE, TRUE, TRUE, TRUE), from = structure(c(2L, 2L, 2L, 2L, 2L), .Label = c(\'CheckExEnv\', \'package:base\', \'package:datasets\', \'package:graphics\', \'package:grDevices\', \'package:methods\', \'package:stats\', \'package:utils\'), class = \'factor\')), .Names = c(\'visible\', \'from\'), row.names = c(\'[[.data.frame\', \'[[.Date\', \'[[.factor\', \'[[.numeric_version\', \'[[.POSIXct\'), class = \'data.frame\'));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames9() {
        assertEval("argv <- list(structure(c(-0.148741651280925, -0.200659450546418, -0.0705810742857073, -0.356547323513813, -0.214670164989233, -0.161150909262745, -0.0362121726544447, -0.259637310505756, -0.142667503568732, -0.113509274827518, -0.0362121726544447, -0.221848749616356, -0.0809219076239261, -0.0969100130080564, 0, -0.113509274827518, -0.0362121726544447, 0, 0.0934216851622351, 0, 0.0644579892269184, 0.113943352306837, 0.161368002234975, 0.0969100130080564, 0.100370545117563, 0.139879086401236, 0.269512944217916, 0.193124598354462, 0.184691430817599, 0.201397124320452, 0.262451089730429, 0.269512944217916, 0.184691430817599, 0.315970345456918, 0.369215857410143, 0.352182518111362, 0.334453751150931, 0.385606273598312, 0.431363764158987, 0.352182518111362, 0.445604203273598, 0.534026106056135, 0.56702636615906, 0.556302500767287, 0.556302500767287, 0.635483746814912, 0.635483746814912, 0.607455023214668, 0.686636269262293, 0.702430536445525, 0.702430536445525, 0.644438589467839, 0.746634198937579, 0.76715586608218, 0.817565369559781, 0.725094521081469, 0.780317312140151, 0.8055008581584, 0.840733234611807, 0.76715586608218, 0.840733234611807, 0.888740960682893, 0.893761762057943, 0.786751422145561, 0.888740960682893, 0.949877704036875, 0.91803033678488, 0.835056101720116, 0.979548374704095, 1.0111473607758, 0.979548374704095, 0.94101424370557, 1.07481644064517, 1.08134730780413, 1.08457627793433, 0.949877704036875, 1.14736710779379, 1.11260500153457, 1.17172645365323, 0.999565488225982, 1.20951501454263, 1.16643011384328, 1.20466251174822, 1.06483221973857, -0.159187512164844, -0.175393808396786, -0.187687755757946, -0.207642507026642, -0.198621515607727, -0.176754605786408, -0.158836651491979, -0.149428697683695, -0.140952828307713, -0.131024527567456, -0.123872539048553, -0.114280817787828, -0.0994047163123464, -0.087368495856282, -0.0695543741081212, -0.0471907302590792, -0.0252858368758202, -0.00046875732235226, 0.0256791224090317, 0.0513817320777501, 0.0766209131410961, 0.0968522890603896, 0.10926580806596, 0.12423159770419, 0.140017729684574, 0.160995251972733, 0.186207523410629, 0.20145502677125, 0.212779940301236, 0.218492043424262, 0.230096897140732, 0.249642407137453, 0.267032743986859, 0.296832600908836, 0.324482776968729, 0.34857076887723, 0.365213423984069, 0.378158595546285, 0.395931013687348, 0.419646007489754, 0.460582747648703, 0.501139500862399, 0.536421474743013, 0.565667794936187, 0.583727648968917, 0.604807814936374, 0.625205889668605, 0.647621016647276, 0.667938096838737, 0.680279300032779, 0.691704012845436, 0.70875844749856, 0.731043761734682, 0.751082618736354, 0.768818358528044, 0.778037539482349, 0.783883770017724, 0.793567437831882, 0.808497952330164, 0.824527436015885, 0.837662628092942, 0.850394585087975, 0.86126020516377, 0.869685339227116, 0.881257746828503, 0.896341202762879, 0.909788792806972, 0.926007182864941, 0.946255773007512, 0.966071629523008, 0.986442844639348, 1.01212132840619, 1.03013203323943, 1.04537749097574, 1.05767268306294, 1.06939407179363, 1.08529788756315, 1.10060754122045, 1.11720247611807, 1.12845012932749, 1.1415063364534, 1.15405124613828, 1.16739846403236, 1.18260472251233, -0.500829194148537, -0.52138894326571, -0.395290556425639, -0.612473674589852, -0.501276350125614, -0.491690167245187, -0.390354415846618, -0.612811414694937, -0.50108691493379, -0.487593021259144, -0.410519772822145, -0.60198159752223, -0.486883827608277, -0.503288363976579, -0.42574385459525, -0.568147157695982, -0.507959098850015, -0.498398837847198, -0.433186387433784, -0.55201418458838, -0.518671455596172, -0.493155285150245, -0.444313944722341, -0.526234257426627, -0.532725645126928, -0.515368327315997, -0.429924874711845, -0.513474228757053, -0.535827355681639, -0.509755301472591, -0.457834384119355, -0.484020307382735, -0.566550518367581, -0.484439669223823, -0.460465869949124, -0.506741194861463, -0.536243725945176, -0.486859832443798, -0.457576573769744, -0.545126578784142, -0.515794992319117, -0.47384137864798, -0.476642594236321, -0.523341985476231, -0.522293038769951, -0.469249989654994, -0.486003635866052, -0.541938916235621, -0.49102139505142, -0.478259440132195, -0.481086490677911, -0.556698657805247, -0.48671796302777, -0.486157330493723, -0.461848039861244, -0.556823945253764, -0.498054862552783, -0.480860984756722, -0.4661755221955, -0.561135228990277, -0.497359948541683, -0.46413941340098, -0.470914891619888, -0.579021563367855, -0.488243746349874, -0.449175409103654, -0.488562751281115, -0.586017616755797, -0.467647247160106, -0.454360847750584, -0.499693656171344, -0.581118529375087, -0.458579410956407, -0.467570268776559, -0.473550583747953, -0.613665700345632, -0.438534120752666, -0.486034341320657, -0.452438304433326, -0.626272401483727, -0.43259461595065, -0.48652221719006, -0.460246994058915, -0.618889869869875), .Dim = c(84L, 3L), .Dimnames = list(NULL, c(\'JJ\', \'sm[, 1]\', \'sm[, 3] - 0.5\')), .Tsp = c(1960, 1980.75, 4), class = c(\'mts\', \'ts\', \'matrix\')));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames10() {
        assertEval("argv <- list(structure(list(Ozone = c(41L, 36L, 12L, 18L, NA, 28L, 23L, 19L, 8L, NA, 7L, 16L, 11L, 14L, 18L, 14L, 34L, 6L, 30L, 11L, 1L, 11L, 4L, 32L, NA, NA, NA, 23L, 45L, 115L, 37L), Solar.R = c(190L, 118L, 149L, 313L, NA, NA, 299L, 99L, 19L, 194L, NA, 256L, 290L, 274L, 65L, 334L, 307L, 78L, 322L, 44L, 8L, 320L, 25L, 92L, 66L, 266L, NA, 13L, 252L, 223L, 279L), Wind = c(7.4, 8, 12.6, 11.5, 14.3, 14.9, 8.6, 13.8, 20.1, 8.6, 6.9, 9.7, 9.2, 10.9, 13.2, 11.5, 12, 18.4, 11.5, 9.7, 9.7, 16.6, 9.7, 12, 16.6, 14.9, 8, 12, 14.9, 5.7, 7.4), Temp = c(67L, 72L, 74L, 62L, 56L, 66L, 65L, 59L, 61L, 69L, 74L, 69L, 66L, 68L, 58L, 64L, 66L, 57L, 68L, 62L, 59L, 73L, 61L, 61L, 57L, 58L, 57L, 67L, 81L, 79L, 76L), Month = c(5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L), Day = 1:31, Oz.Z = c(0.782229292792786, 0.557251841325834, -0.522639925715534, -0.252666983955192, NA, 0.197287918978711, -0.0276895324882403, -0.207671493661802, -0.702621886889095, NA, -0.747617377182486, -0.342657964541973, -0.567635416008924, -0.432648945128753, -0.252666983955192, -0.432648945128753, 0.467260860739053, -0.792612867475876, 0.287278899565492, -0.567635416008924, -1.01759031894283, -0.567635416008924, -0.882603848062657, 0.377269880152273, NA, NA, NA, -0.0276895324882403, 0.962211253966347, 4.11189557450367, 0.602247331619224)), .Names = c(\'Ozone\', \'Solar.R\', \'Wind\', \'Temp\', \'Month\', \'Day\', \'Oz.Z\'), row.names = c(NA, 31L), class = \'data.frame\'));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames11() {
        assertEval("argv <- list(structure(list(c0 = structure(integer(0), .Label = character(0), class = \'factor\')), .Names = \'c0\', row.names = character(0), class = \'data.frame\'));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames12() {
        assertEval("argv <- list(structure(logical(0), .Dim = c(0L, 4L), .Dimnames = list(NULL, c(\'Estimate\', \'Std. Error\', \'t value\', \'Pr(>|t|)\'))));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames13() {
        assertEval("argv <- list(structure(c(1.00000000141919, 1.00000000141919, 1.00000000141919, 1.00000000141919, 1.00000000141919, 1.00000003943081, 0.999999963407572, 1.00000003943081, 0.999999887384338, 0.999999887384338, 1.00000110375608, 0.999998671012596, 1.00000353649956, 0.999993805525628, 1.0000132674735, 0.999993805525628, 0.999954881629886, 1.00003272942137, 1.00018842500434, 1.00018842500434, 0.998942860340576, 0.998942860340576, 1.00143398966811, 1.00641624832317, 0.996451731013043, 0.996451731013043, 0.996451731013043, 0.956593661772521, 1.11602593873461, 0.956593661772521, 0.637729107848348, 1.2754582156967, 0, 5.10183286278678, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.94013552497596, 3.76412613616612, 7.30291496677768, 14.1686451539795, 27.4890922070583, 53.3325661305982, 103.472408672272, 200.750502109581, 389.483191189237, 755.650202381997, 1466.06384193569, 2844.36258373045, 5518.44906703615, 10706.5393765913, 20772.1380196383, 40300.7636161042, 78188.9463884664, 151697.155845212, 294313.049942543, 571007.218374244, 1107831.42112573, 2149343.17429151, 4170017.13104157, 8090398.61389002, 15696470.311059, 30453280.1141634, 59083492.559312, 114629985.805561, 222397713.850125, 431481718.951103, 837133036.93017, 1624151587.48205, 3151074263.75394, 6113511302.83196, 11861040827.0204, 23012027206.5324, 44646452442.3573, 86620170794.9288, 168054874821.487, 326049245490.904, 632579733498.458, 1227290450360.24, 2381109863489.76, 4619676014664.61, 8962797733853.2, 17389042711398.3, 33737100912048.4, 65454549040847.5, 126990699928896, 246379175201939, 478009006423530, 927402279739830, 1799286146937082, 3490859083252747, 6772739936266967, 13140033736528760, 25493446790450148, 49460743386475336, 95960547120913696, 186176471759588736, 361207596842468544, 700791709787994368, 1359630936550262016, 2637868362903399936, 5117822236182886400, 9929268960054196224, 19264127970060791808, 37375020125774807040, 72512606973297393664, 1.40684287014036e+20, 2.7294659047997e+20, 5.29553390729944e+20, 1.02740538360686e+21, 1.99330573047582e+21, 3.86728338996836e+21, 7.50305404171627e+21, 1.45569421000466e+22, 2.82424412804067e+22, 5.47941653460064e+22, 1.06308109216746e+23, 2.06252146205912e+23, 4.00157124688985e+23, 7.76359070435321e+23, 1.50624186823988e+24, 2.9223134410489e+24, 5.66968428254813e+24, 1.09999562078627e+25, 2.13414063286361e+25, 4.14052220754691e+25, 8.03317433205064e+25, 1.55854473744651e+26, 3.02378808245954e+26, 5.8665589378512e+26, 1.1381919629553e+27, 2.20824672789753e+27, 4.28429802210817e+27, 8.31211904202342e+27, 1.6126637849175e+28, 3.12878639712715e+28, 6.07026977774323e+28, -1.69869203952449, -6.59138547321901, -19.1822720729097, -49.6216113971901, -120.340815686727, -280.172995242172, -634.16918707159, -1406.14196698717, -3069.11929954479, -6616.11942037001, -14119.7855095835, -29884.6887074354, -62812.0433183954, -131238.022320374, -272806.670597037, -564567.387887297, -1163795.8381508, -2390740.65186453, -4896047.65118829, -9998943.37840444, -20369271.0188994, -41401011.400719, -83974646.5467737, -170005771.46876, -343577337.860415, -693250079.585253, -1396729874.79437, -2810209917.82931, -5646909305.11788, -11333554766.2687, -22721587164.2141, -45504990439.9914, -91044782407.2254, -181991924578.337, -363473977210.141, -725337050553.89, -1446342551648.47, -2881941162160.65, -5738497521398.03, -11418936575319.8, -22708142164275.2, -45131432245311.6, -89645884346409.8, -177969941625094, -353133219287688, -700351348689777, -1388315183960579, -2750828581307066, -5448167546359572, -10785901526178346, -21344633431230288, -42223472396959864, -83494631109252016, -165047335865753888, -326144099824872960, -644268556323104896, -1272289222665704960, -2511719077288045568, -4957094068295422976, -9780441891046391808, -1.9291639356539e+19, -38041976425069862912, -74997019625717596160, -1.47813980522732e+20, -2.91260085798056e+20, -5.73777646929122e+20, -1.13007319819804e+21, -2.22521899361014e+21, -4.38071519248462e+21, -8.62235781974388e+21, -1.69675222834625e+22, -3.33829458816692e+22, -6.56669878434099e+22, -1.2914810481395e+23, -2.53950840681221e+23, -4.99268379737403e+23, -9.81393715458217e+23, -1.92876460684381e+24, -3.79004004348997e+24, -7.44626980645141e+24, -1.46273574426805e+25, -2.87294154406129e+25, -5.64187042306716e+25, -1.10778729236262e+26, -2.1748439290622e+26, -4.26913311245721e+26, -8.37900750248964e+26, -1.64432657900359e+27, -3.22646894393502e+27, -6.33012184999843e+27, -1.24177532894999e+28, -2.4356873672908e+28, -4.77692848222753e+28, -9.36754365223252e+28, -1.83676481719521e+29, -3.60108404728876e+29, -7.05936824038065e+29, -1.38373288587828e+30, -2.71202352823357e+30, -5.31484173040654e+30), .Dim = c(100L, 3L)));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames14() {
        assertEval("argv <- list(structure(c(0.0267062896727757, 0.0288568829806391, 0.0492797675063702, -0.0572758706635325, 0.0323212080834915, 0.0153959090462038, -0.0926263811460125, 0.063635852899097, -0.0487298690847772, 0.0550557154550753, -0.0198673280094324, -0.0926263811460125, 0.0140766975562449, 0.0582283948223117, 0.00131213541438663, 0.091788691086849, -0.047804624481445, 0.0620410336020094, -0.0572622511611348, 0.0287132484691924, -0.083909175260886, -0.078402304235417, 0.0355405280997754, 0.0717493130601819, -0.0215453924782896, 0.0433187167027035, -0.0391463972547204, 0.0355405280997753, -0.101433295142967, -0.0534964348088565, -0.0100532600167173, 0.115085165377514), .Dim = c(16L, 2L), .Dimnames = list(structure(c(\'M1\', \'M2\', \'M4\', \'M5\', \'BF\', \'HF\', \'NM\', \'SF\', \'U1\', \'U2\', \'U3\', \'C0\', \'C1\', \'C2\', \'C3\', \'C4\'), .Names = c(\'Mois1\', \'Mois2\', \'Mois3\', \'Mois4\', \'Manag1\', \'Manag2\', \'Manag3\', \'Manag4\', \'Use1\', \'Use2\', \'Use3\', \'Manure1\', \'Manure2\', \'Manure3\', \'Manure4\', \'Manure5\')), c(\'1\', \'2\'))));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames15() {
        assertEval("argv <- list(structure(\'foo\', .Dim = c(1L, 1L), .Dimnames = list(NULL, structure(\'object\', simpleOnly = TRUE))));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames16() {
        assertEval("argv <- list(structure(c(\'4.1-0\', \'4.1-0\', \'4.1-0\', \'4.1-0\', \'4.1-0\', \'4.1-0\', \'4.0-3\', \'4.0-3\', \'4.0-3\', \'4.0-3\', \'4.0-3\', \'4.0-2\', \'4.0-2\', \'4.0-1\', \'4.0-1\', \'4.0-1\', \'4.0-1\', \'4.0-1\', \'4.0-1\', \'4.0-1\', \'4.0-1\', \'3.1-55\', \'3.1-55\', \'3.1-55\', \'3.1-54\', \'3.1-53\', \'3.1-53\', \'3.1-52\', \'3.1-51\', NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, \'The C and R code has been reformatted for legibility.\', \'The old compatibility function rpconvert() has been removed.\', \'The cross-validation functions allow for user interrupt at the end\\nof evaluating each split.\', \'Variable Reliability in data set car90 is corrected to be an\\nordered factor, as documented.\', \'Surrogate splits are now considered only if they send two or more\\ncases _with non-zero weight_ each way.  For numeric/ordinal\\nvariables the restriction to non-zero weights is new: for\\ncategorical variables this is a new restriction.\', \'Surrogate splits which improve only by rounding error over the\\ndefault split are no longer returned.  Where weights and missing\\nvalues are present, the splits component for some of these was not\\nreturned correctly.\', \'A fit of class \\\'rpart\\\' now contains a component for variable\\n‘importance’, which is reported by the summary() method.\', \'The text() method gains a minlength argument, like the labels()\\nmethod.  This adds finer control: the default remains pretty =\\nNULL, minlength = 1L.\', \'The handling of fits with zero and fractional weights has been\\ncorrected: the results may be slightly different (or even\\nsubstantially different when the proportion of zero weights is\\nlarge).\', \'Some memory leaks have been plugged.\', \'There is a second vignette, longintro.Rnw, a version of the\\noriginal Mayo Tecnical Report on rpart.\', \'Added dataset car90, a corrected version of the S-PLUS dataset\\ncar.all (used with permission).\', \'This version does not use paste0{} and so works with R 2.14.x.\', \'Merged in a set of Splus code changes that had accumulated at Mayo\\nover the course of a decade. The primary one is a change in how\\nindexing is done in the underlying C code, which leads to a major\\nspeed increase for large data sets.  Essentially, for the lower\\nleaves all our time used to be eaten up by bookkeeping, and this\\nwas replaced by a different approach.  The primary routine also\\nuses .Call{} so as to be more memory efficient.\', \'The other major change was an error for asymmetric loss matrices,\\nprompted by a user query.  With L=loss asymmetric, the altered\\npriors were computed incorrectly - they were using L' instead of L.\\nUpshot - the tree would not not necessarily choose optimal splits\\nfor the given loss matrix.  Once chosen, splits were evaluated\\ncorrectly.  The printed “improvement” values are of course the\\nwrong ones as well.  It is interesting that for my little test\\ncase, with L quite asymmetric, the early splits in the tree are\\nunchanged - a good split still looks good.\', \'Add the return.all argument to xpred.rpart().\', \'Added a set of formal tests, i.e., cases with known answers to\\nwhich we can compare.\', \'Add a usercode vignette, explaining how to add user defined\\nsplitting functions.\', \'The class method now also returns the node probability.\', \'Add the stagec data set, used in some tests.\', \'The plot.rpart routine needs to store a value that will be visible\\nto the rpartco routine at a later time.  This is now done in an\\nenvironment in the namespace.\', \'Force use of registered symbols in R >= 2.16.0\', \'Update Polish translations.\', \'Work on message formats.\', \'Add Polish translations\', \'rpart, rpart.matrix: allow backticks in formulae.\', \'tests/backtick.R: regession test\', \'src/xval.c: ensure unused code is not compiled in.\', \'Change description of margin in ?plot.rpart as suggested by Bill\\nVenables.\'), .Dim = c(29L, 4L)));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames17() {
        assertEval("argv <- list(structure(c(0, 0, 0, 0, 0, -1.43884556914512e-134, 0, 0, 0, -7.95468296571581e-252, 1.76099882882167e-260, 0, -9.38724727098368e-323, -0.738228974836154, 0, 0, 0, 0, 0, 0, 0, 0, 0, -6.84657791618065e-123, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1.05931985100232e-174, 0, -3.41789378681991e-150, 0, 0, 0, 0, -1.07225492686949e-10, 0, 1.65068934474523e-67, 0, -6.49830035279282e-307, 0, 5.83184963977238e-90, 0, -9.81722610183938e-287, 6.25336419454196e-54, 0, 0, 0, -1.72840591500382e-274, 1.22894687952101e-13, 0.660132850077566, 0, 0, 7.79918925397516e-200, -2.73162827952857e-178, 1.32195942051179e-41, 0, 0, 0, 0, 2.036057023761e-45, -3.40425060445074e-186, 1.59974269220388e-26, 0, 6.67054294775317e-124, 0.158503117506202, 0, 0, 0, 0, 0, 0, 3.42455724859116e-97, 0, 0, -2.70246891320217e-272, 0, 0, -3.50562438899045e-06, 0, 0, 1.35101732326608e-274, 0, 0, 0, 0, 0, 0, 0, 7.24580295957621e-65, 0, -3.54887341172294e-149, 0, 0, 0, 0, 0, 0, 0, 0, 1.77584594753563e-133, 0, 0, 0, 2.88385135688311e-250, 1.44299633616158e-259, 0, 1.56124744085834e-321, 1.63995835868977, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2.01050064173383e-122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.64868196850938e-172, 0, 6.28699823828692e-149, 0, 0, 0, 0, 5.0552295590188e-09, 0, 2.30420733561404e-66, 0, 7.0823279075443e-306, 0, 2.05009901740696e-88, 0, 7.41800724282869e-285, 7.18347043784483e-53, 0, 0, 0, 1.04251223075649e-273, 9.75816316577433e-13, 4.29519957592147, 0, 0, 1.33541454912682e-198, 2.34606233784019e-176, 8.38236726536896e-41, 0, 0, 0, 0, 1.35710537434521e-43, 1.15710503176511e-185, 1.25601735272233e-25, 0, 4.46811655846376e-123, 4.4196641795634, 0, 0, 0, 0, 0, 0, 3.74179015251531e-93, 0, 0, 3.62662047836582e-271, 0, 0, 1.26220330674453e-05, 0, 0, 1.72715562657338e-273, 0, 0, 0, 0, 0, 0, 0, 5.46372806810809e-64, 0, 2.47081972486962e-148, 0, 0, 0), .Dim = c(100L, 2L)));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames18() {
        assertEval("argv <- list(structure(list(Ozone = c(39L, 9L, 16L, 78L, 35L, 66L, 122L, 89L, 110L, NA, NA, 44L, 28L, 65L, NA, 22L, 59L, 23L, 31L, 44L, 21L, 9L, NA, 45L, 168L, 73L, NA, 76L, 118L, 84L, 85L), Solar.R = c(83L, 24L, 77L, NA, NA, NA, 255L, 229L, 207L, 222L, 137L, 192L, 273L, 157L, 64L, 71L, 51L, 115L, 244L, 190L, 259L, 36L, 255L, 212L, 238L, 215L, 153L, 203L, 225L, 237L, 188L), Wind = c(6.9, 13.8, 7.4, 6.9, 7.4, 4.6, 4, 10.3, 8, 8.6, 11.5, 11.5, 11.5, 9.7, 11.5, 10.3, 6.3, 7.4, 10.9, 10.3, 15.5, 14.3, 12.6, 9.7, 3.4, 8, 5.7, 9.7, 2.3, 6.3, 6.3), Temp = c(81L, 81L, 82L, 86L, 85L, 87L, 89L, 90L, 90L, 92L, 86L, 86L, 82L, 80L, 79L, 77L, 79L, 76L, 78L, 78L, 77L, 72L, 75L, 79L, 81L, 86L, 88L, 97L, 94L, 96L, 94L), Month = c(8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 8L), Day = 1:31, Oz.Z = c(-0.528248463997741, -1.28427378861836, -1.10786787954022, 0.454584458009066, -0.629051840613824, 0.152174328160817, 1.56342160078598, 0.731793743703293, 1.26101147093773, NA, NA, -0.402244243227638, -0.805457749691969, 0.126973484006797, NA, -0.956662814616093, -0.0242315809173275, -0.931461970462072, -0.729855217229907, -0.402244243227638, -0.981863658770114, -1.28427378861836, NA, -0.377043399073617, 2.72266043187093, 0.328580237238962, NA, 0.404182769701024, 1.46261822416989, 0.60578952293319, 0.63099036708721)), .Names = c(\'Ozone\', \'Solar.R\', \'Wind\', \'Temp\', \'Month\', \'Day\', \'Oz.Z\'), row.names = 93:123, class = \'data.frame\'));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames19() {
        assertEval("argv <- list(structure(c(28L, 138L, 16L), .Dim = 3L, .Dimnames = structure(list(object = c(\'FALSE\', \'TRUE\', NA)), .Names = \'object\'), class = \'table\'));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames20() {
        assertEval("argv <- list(structure(c(\'myTst\', \'myLib\', \'1.0\', NA, \'methods\', NA, NA, NA, NA, \'What license is it under?\', NA, NA, NA, NA, NA, NA, \'3.0.1\'), .Dim = c(1L, 17L), .Dimnames = list(\'ret0\', c(\'Package\', \'LibPath\', \'Version\', \'Priority\', \'Depends\', \'Imports\', \'LinkingTo\', \'Suggests\', \'Enhances\', \'License\', \'License_is_FOSS\', \'License_restricts_use\', \'OS_type\', \'Archs\', \'MD5sum\', \'NeedsCompilation\', \'Built\'))));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames21() {
        assertEval("argv <- list(structure(list(Df = c(NA, 1, 1, 2), Deviance = c(12.2441566485997, 32.825622681839, 8.44399377410362, 11.9670615295804), AIC = c(73.9421143635373, 92.5235803967766, 72.1419514890412, 77.665019244518)), .Names = c(\'Df\', \'Deviance\', \'AIC\'), row.names = c(\'<none>\', \'- M.user\', \'+ Temp\', \'+ Soft\'), class = c(\'anova\', \'data.frame\')));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames22() {
        assertEval("argv <- list(structure(c(1L, 2L, 1L), .Dim = 3L, .Dimnames = structure(list(c(\'1\', \'2\', NA)), .Names = \'\'), class = \'table\'));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames23() {
        assertEval("argv <- list(structure(c(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), .Dim = c(10L, 2L), .Dimnames = list(NULL, c(\'tt\', \'tt + 1\')), .Tsp = c(1920.5, 1921.25, 12), class = c(\'mts\', \'ts\', \'matrix\')));dimnames(argv[[1]]);");
    }

    @Test
    public void testdimnames24() {
        assertEval("argv <- list(structure(c(1L, 1L, 1L, 1L, 2L, 1L, NA), .Label = c(\'no\', \'yes\'), class = \'factor\'));dimnames(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testdimnames26() {
        assertEval("argv <- list(structure(c(-15.7095066647243, 0.26727943386171, 0.297238382214578, 0.257897591876632, 0.340108731286838, 0.236310380889319, 0.317311605722827, 0.262866287094154, 0.338086383499512, 0.234905236792884, 0.325336667185977, 0.218927692395608, -7.51574917378772, 7.84743436370915, -0.381048703752012, -0.330615253498497, 0.244844953659604, 0.170120314286586, -0.406781840034597, -0.336984938523255, 0.243389061455961, 0.169108748250409, -0.417069674483433, -0.280657271719851, -5.36168424071406, 0.204399594459056, 7.44580265802875, 0.18731755950565, -0.56882795084156, -0.395226400731518, -0.439007571789656, -0.363681278343691, 0.147865047400615, 0.10273786720867, 0.236300269698257, 0.159012733501467, -5.07819471343419, -0.0276453301370831, -3.65602301353979, 6.37342950130462, 0.0099206539914638, 0.0068929530698134, 0.118301269982087, 0.0980027677458417, -0.620575419067553, -0.431180972906935, -0.48536920518568, -0.326617841666301), .Dim = c(12L, 4L), .Dimnames = list(c(\'1\', \'3\', \'5\', \'7\', \'9\', \'11\', \'13\', \'15\', \'17\', \'19\', \'21\', \'23\'), c(\'(Intercept)\', \'M.userY\', \'SoftMedium\', \'SoftSoft\'))));dimnames(argv[[1]]);");
    }
}
